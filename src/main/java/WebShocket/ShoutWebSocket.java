package WebShocket;

import dto.ChatDTO;
import dto.CustomerDTO;
import dto.MessageDTO;
import dto.UserDTO;
import entity.Chat;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IChatService;
import service.ICustomerService;
import service.IMessageService;
import service.Impl.ChatService;
import service.Impl.CustomerServiceImpl;
import service.Impl.MessageService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/chat")
public class ShoutWebSocket {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final IMessageService messageService = new MessageService();
    private IChatService chatService = new ChatService();
    private static final ICustomerService customerService = new CustomerServiceImpl();
    private static ConcurrentHashMap<Integer, Set<Session>> chatSessions = new ConcurrentHashMap<>();  // Lưu session theo chatId
    private static ConcurrentHashMap<Integer, List<MessageDTO>> messageCache = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // Khi kết nối, yêu cầu gửi userId
        try {
            session.getBasicRemote().sendText("Please send your userId to continue...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String messageContent, Session session) {
        try {
            System.out.println("Received message: " + messageContent);  // Debug: in ra nội dung tin nhắn

            if (messageContent.startsWith("user:")) {
                // Xử lý khi người dùng gửi thông tin userId
                String userJson = messageContent.substring(5); // Cắt bỏ "user:" phía trước
                User user = objectMapper.readValue(userJson, User.class); // Chuyển JSON thành đối tượng User
                int userId = user.getUserID();
                session.getUserProperties().put("userId", userId);  // Lưu userId vào session
                System.out.println("User ID: " + userId);  // Debug: in ra userId

                // Nếu là admin, gửi danh sách khách hàng
                if ("ADMIN".equals(user.getAccount().getRole().name().toString())) {
                    List<CustomerDTO> customerList = customerService.GetAllCustomer();
                    StringBuilder customerListHtml = new StringBuilder();
                    for (CustomerDTO customer : customerList) {
                        customerListHtml.append("<div class='customer-item' onclick='switchChat(")
                                .append(customer.getUserID())
                                .append(")'>")
                                .append(customer.getFullName())
                                .append("</div>");
                    }
                    session.getBasicRemote().sendText(customerListHtml.toString());
                } else {
                    // Nếu là customer, lấy tin nhắn liên quan
                    loadMessagesForCustomer(session, userId);
                }
                return;
            }

            // Xử lý khi admin chuyển sang cuộc trò chuyện khác
            if (messageContent.startsWith("switchChat:")) {
                int customerId = Integer.parseInt(messageContent.split(":")[1]);
                switchChat(session, customerId);
                return;
            }

            // Xử lý tin nhắn bình thường
            handleMessage(session, messageContent);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMessagesForCustomer(Session session, int userId) throws IOException, SQLException {
        ChatDTO chat = chatService.getOrCreateChatId(userId);
        chatSessions.computeIfAbsent(chat.getChatId(), k -> new HashSet<>()).add(session);
        session.getUserProperties().put("chat", chat);
        List<MessageDTO> recentMessages = messageService.getRecentMessages(chat.getChatId());
        if (recentMessages != null && !recentMessages.isEmpty()) {
            messageCache.computeIfAbsent(chat.getChatId(), k -> new CopyOnWriteArrayList<>()).addAll(recentMessages);
            sendMessagesToSession(chat.getChatId(), session);
        } else {
            session.getBasicRemote().sendText("No previous messages.");
        }
    }

    private void switchChat(Session session, int customerId) throws IOException, SQLException {
        // Lưu chatId vào session khi admin chọn customer
        ChatDTO chat = chatService.getOrCreateChatId(customerId);
        session.getUserProperties().put("chat", chat);

        // Lấy và gửi các tin nhắn cho customer và admin trong cuộc trò chuyện này
        loadMessagesForCustomer(session, customerId);
    }

    private void handleMessage(Session session, String messageContent) throws IOException, SQLException {
        int userId = (int) session.getUserProperties().get("userId");
        ChatDTO chat = (ChatDTO) session.getUserProperties().get("chat");
        if (chat == null) {
            session.getBasicRemote().sendText("Error: Chat not initialized.");
            return;
        }

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatId(chat.getChatId());
        messageDTO.setUserId(userId);
        messageDTO.setContent(messageContent);
        messageDTO.setDate(new Timestamp(System.currentTimeMillis()));

        // Lưu tin nhắn vào cache và database
        messageCache.computeIfAbsent(chat.getChatId(), k -> new CopyOnWriteArrayList<>()).add(0, messageDTO);
        broadcastMessages(chat.getChatId());
        messageService.saveMessage(messageDTO);

    }

    private void broadcastMessages(int chatId) throws IOException {
        // Debug: in ra trước khi phát sóng tin nhắn
        System.out.println("Broadcasting messages for chatId: " + chatId);

        List<MessageDTO> messages = messageCache.get(chatId);
        if (messages == null || messages.isEmpty()) {
            System.out.println("No messages found for chatId: " + chatId);  // Debug: không có tin nhắn
            return;
        }

        Set<Session> sessions = chatSessions.get(chatId);
        if (sessions == null) {
            System.out.println("No sessions found for chatId: " + chatId);  // Debug: không có session
            return;
        }

        // Gửi tin nhắn tới tất cả các session trong chat
        for (Session s : sessions) {
            if (s.isOpen()) {
                sendMessagesToSession(chatId, s);
            }
        }
    }

    private void sendMessagesToSession(int chatId, Session session) throws IOException {
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
            String userId = String.valueOf(session.getUserProperties().get("userId"));
            String messageClass = (message.getUserId() == Integer.parseInt(userId)) ? "right" : "left";
            String userName = "User " + message.getUserId();

            // Tạo HTML cho từng tin nhắn
            messagesHtml.append("<div class='message ").append(messageClass).append("'>")
                    .append("<b>").append(userName).append(":</b> ")
                    .append(message.getContent())
                    .append("<div class='time'>").append(messageTime).append("</div>")
                    .append("</div>");
        }

        // Gửi HTML tin nhắn tới client
        session.getBasicRemote().sendText(messagesHtml.toString());
    }

    @OnClose
    public void onClose(Session session) {
        // Xóa session khỏi chatSessions khi kết thúc kết nối
        chatSessions.values().forEach(sessionSet -> sessionSet.remove(session));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}
