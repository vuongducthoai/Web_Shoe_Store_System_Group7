package WebShocket;

import dto.CustomerDTO;
import dto.MessageDTO;
import dto.UserDTO;
import entity.Chat;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICustomerService;
import service.IMessageService;
import service.Impl.CustomerServiceImpl;
import service.Impl.MessageService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ShoutWebSocket {

    private static final IMessageService messageService = new MessageService();
    private static final ICustomerService customerService = new CustomerServiceImpl();// Use the MessageServiceImpl
    private static Set<Session> clients = new CopyOnWriteArraySet<>();
    private static ConcurrentHashMap<Integer, List<MessageDTO>> messageCache = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("New connection: " + session.getId());
        try {
            session.getBasicRemote().sendText("Please send your userId to continue...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String messageContent, Session session) {
        try {
            // Retrieve Chat object from session
            Chat chat = (Chat) session.getUserProperties().get("chat");

            // Handle userId connection
            if (messageContent.startsWith("userId:")) {
                String userId = messageContent.split(":")[1].trim();
                session.getUserProperties().put("userId", userId);
                System.out.println("User connected with userId: " + userId);

                // If the user is admin (userId = "1"), fetch and send customer list
                if ("1".equals(userId)) {
                    List<CustomerDTO> customerList = customerService.GetAllCustomer(); // Assuming this method fetches customers
                    StringBuilder customerListHtml = new StringBuilder();

                    // Build the HTML to send back to the admin
                    for (CustomerDTO customer : customerList) {
                        customerListHtml.append("<div class='customer-item'>")
                                .append(customer.getFullName())
                                .append("</div>");
                    }

                    // Send the customer list to the admin
                    session.getBasicRemote().sendText(customerListHtml.toString());
                }
                int chatId=1;
                messageService.getRecentMessages()
            }

            // Retrieve userId from session properties
            String userId = (String) session.getUserProperties().get("userId");
            if (userId == null) {
                session.getBasicRemote().sendText("Error: You need to connect first with a valid userId.");
                return;
            }

            // Check if chat is initialized
            if (chat == null) {
                session.getBasicRemote().sendText("Error: Chat not initialized.");
                return;
            }

            // Handle "loadMoreMessages" request
            if (messageContent.equals("loadMoreMessages")) {
                List<MessageDTO> lastMessages = messageCache.get(chat.getChatID());
                Timestamp lastMessageTimestamp = lastMessages.get(lastMessages.size() - 1).getDate();

                List<MessageDTO> moreMessages = messageService.getMessages(chat.getChatID(), lastMessageTimestamp);
                if (moreMessages.isEmpty()) {
                    session.getBasicRemote().sendText("noMoreMessages");
                } else {
                    messageCache.get(chat.getChatID()).addAll(moreMessages);
                    sendCachedMessages(chat.getChatID(), session);
                }
                return;
            }

            // Handle normal message sending
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setChat(chat); // Assign the Chat object from session
            messageDTO.setUserId(Integer.parseInt(userId));
            messageDTO.setContent(messageContent);
            messageDTO.setDate(new Timestamp(System.currentTimeMillis()));

            // Cache the message and save it
            messageCache.computeIfAbsent(chat.getChatID(), k -> new CopyOnWriteArrayList<>()).add(0, messageDTO);
            messageService.saveMessage(messageDTO);

            // Broadcast new messages to all clients
            broadcastMessages(chat.getChatID());

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void broadcastMessages(int chatId) {
        List<MessageDTO> messages = messageCache.get(chatId);
        String currentDate = ""; // Biến lưu trữ ngày hiện tại để kiểm tra sự thay đổi ngày

        for (Session s : clients) {
            if (s.isOpen()) {
                try {
                    String userId = (String) s.getUserProperties().get("userId");
                    StringBuilder messagesHtml = new StringBuilder();

                    for (int i = messages.size() - 1; i >= 0; i--) {
                        MessageDTO message = messages.get(i);
                        String messageDate = message.getDate().toString().split(" ")[0]; // Lấy ngày từ Timestamp

                        // Chỉ hiển thị ngày nếu ngày tin nhắn thay đổi
                        if (!messageDate.equals(currentDate)) {
                            currentDate = messageDate;
                            messagesHtml.append("<div class='date-separator'><span>"+ currentDate + "</span></div>");
                        }

                        // Chỉ hiển thị giờ
                        String messageTime = message.getDate().toString().split(" ")[1].substring(0, 5); // Lấy giờ và phút
                        String messageClass = (message.getUserId() == Integer.parseInt(userId)) ? "right" : "left";
                        String userName = "User " + message.getUserId();

                        messagesHtml.append("<div class='message ").append(messageClass).append("'>")
                                .append("<b>").append(userName).append(":</b> ").append(message.getContent())
                                .append("<div class='time'>").append(messageTime).append("</div>")
                                .append("</div>");
                    }

                    // Gửi HTML tin nhắn tới client
                    s.getBasicRemote().sendText(messagesHtml.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendCachedMessages(int chatId, Session session) throws IOException {
        List<MessageDTO> messages = messageCache.get(chatId);
        StringBuilder messagesHtml = new StringBuilder();
        String currentDate = ""; // Biến để lưu ngày hiện tại

        // Duyệt ngược từ cuối đến đầu danh sách tin nhắn
        for (int i = messages.size() - 1; i >= 0; i--) {
            MessageDTO message = messages.get(i);
            String messageDate = message.getDate().toString().split(" ")[0]; // Lấy ngày từ Timestamp

            // Chỉ hiển thị ngày nếu ngày tin nhắn thay đổi
            if (!messageDate.equals(currentDate)) {
                currentDate = messageDate;
                messagesHtml.append("<div class='date-separator'>" + currentDate + "</div>");
            }

            // Chỉ hiển thị giờ
            String messageTime = message.getDate().toString().split(" ")[1].substring(0, 5); // Lấy giờ và phút
            String userId = (String) session.getUserProperties().get("userId");
            String messageClass = (message.getUserId() == Integer.parseInt(userId)) ? "right" : "left";
            String userName = "User " + message.getUserId();

            messagesHtml.append("<div class='message ").append(messageClass).append("'>")
                    .append("<b>").append(userName).append(":</b> ").append(message.getContent())
                    .append("<div class='time'>").append(messageTime).append("</div>")
                    .append("</div>");
        }

        // Gửi HTML tin nhắn tới client
        session.getBasicRemote().sendText(messagesHtml.toString());
    }
}