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

            if (messageContent.startsWith("userId:")) {
                Chat chat;
                String userId;

                userId = messageContent.split(":")[1].trim();
                session.getUserProperties().put("userId", userId);
                System.out.println("User connected with userId: " + userId);
                if (userId == "") {
                    session.getBasicRemote().sendText("Error: You need to connect first with a valid userId.");
                    return;
                }
                int chatId = 1; // Gán chatId cố định, ví dụ chatId = 1
                // Lấy danh sách tin nhắn gần đây từ cache hoặc service
                List<MessageDTO> recentMessages = messageService.getRecentMessages(chatId);
                if (recentMessages != null && !recentMessages.isEmpty()) {
                    // Lưu tin nhắn vào cache
                    messageCache.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).addAll(recentMessages);

                    // Lấy đối tượng Chat từ tin nhắn đầu tiên trong danh sách tin nhắn
                    chat = recentMessages.get(0).getChat();
                    session.getUserProperties().put("chat", chat);  // Lưu Chat vào session

                    sendCachedMessages(chatId, session);  // Gửi tin nhắn từ cache
                } else {
                    session.getBasicRemote().sendText("No previous messages.");
                }

                // Nếu user là admin (userId = "1"), gửi danh sách khách hàng
                if ("1".equals(userId)) {
                    List<CustomerDTO> customerList = customerService.GetAllCustomer(); // Giả sử có phương thức để lấy khách hàng
                    StringBuilder customerListHtml = new StringBuilder();
                    for (CustomerDTO customer : customerList) {
                        customerListHtml.append("<div class='customer-item'>")
                                .append(customer.getFullName())
                                .append("</div>");
                    }
                    // Gửi danh sách khách hàng cho admin
                    session.getBasicRemote().sendText(customerListHtml.toString());
                }

                return;
            }

            // Kiểm tra nếu chat chưa được khởi tạo
            if (session.getUserProperties().get("chat") == null) {
                session.getBasicRemote().sendText("Error: Chat not initialized.");
                return;
            }
            Chat chat = (Chat) session.getUserProperties().get("chat");
            String userId = (String) session.getUserProperties().get("userId");
            if (chat == null) {
                session.getBasicRemote().sendText("Error: Chat not initialized.");
                return;
            }
            // Xử lý yêu cầu "loadMoreMessages" để tải thêm tin nhắn
            if (messageContent.equals("loadMoreMessages")) {
                List<MessageDTO> lastMessages = messageCache.get(chat.getChatID());
                if (lastMessages == null || lastMessages.isEmpty()) {
                    session.getBasicRemote().sendText("No messages available.");
                    return;
                }

                // Lấy thời gian của tin nhắn cuối cùng trong cache
                Timestamp lastMessageTimestamp = lastMessages.get(lastMessages.size() - 1).getDate();

                // Lấy thêm tin nhắn từ service
                List<MessageDTO> moreMessages = messageService.getMessages(chat.getChatID(), lastMessageTimestamp);
                if (moreMessages.isEmpty()) {
                    session.getBasicRemote().sendText("noMoreMessages");
                } else {
                    // Thêm tin nhắn mới vào cache và gửi lại
                    messageCache.get(chat.getChatID()).addAll(moreMessages);
                    sendCachedMessages(chat.getChatID(), session);
                }
                return;
            }

            // Xử lý gửi tin nhắn bình thường
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setChat(chat);  // Gán đối tượng Chat từ session
            messageDTO.setUserId(Integer.parseInt(userId));
            messageDTO.setContent(messageContent);
            messageDTO.setDate(new Timestamp(System.currentTimeMillis()));

            // Lưu tin nhắn vào cache và database
            messageCache.computeIfAbsent(chat.getChatID(), k -> new CopyOnWriteArrayList<>()).add(0, messageDTO);
            messageService.saveMessage(messageDTO);

            // Phát sóng tin nhắn mới tới tất cả người dùng trong chat
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