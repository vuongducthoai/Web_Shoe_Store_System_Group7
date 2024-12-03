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
import service.IUserService;
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
import service.Impl.UserServiceImpl;

@ServerEndpoint("/chat")
public class ShoutWebSocket {

    private UserDTO UserChat = null;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final IMessageService messageService = new MessageService();
    private IChatService chatService = new ChatService();
    private IUserService userService = new UserServiceImpl();
    private static final ICustomerService customerService = new CustomerServiceImpl();
    private static ConcurrentHashMap<Integer, Set<Session>> chatSessions = new ConcurrentHashMap<>();
    // Cấu trúc để lưu session của admin theo chatId
    //   private static ConcurrentHashMap<Integer, Set<Session>> adminSessionsMap = new ConcurrentHashMap<>();
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
                UserDTO user = objectMapper.readValue(userJson, UserDTO.class);
                session.getUserProperties().put("user", user);
                int userId = user.getUserID();
                session.getUserProperties().put("userId", userId);  // Lưu userId vào session
                System.out.println("User ID: " + userId);  // Debug: in ra userId

                // Nếu là admin, gửi danh sách khách hàng
                if ("ADMIN".equals(user.getAccount().getRole().name().toString())) {
                    sendListCusToAdmin(session);
                } else {
                    // Nếu là customer, lấy tin nhắn liên quan
                    loadMessagesForCustomer(session, userId, -1);
                }
                //
                return;
            }
            if (messageContent.equals("loadMoreMessages")) {
                int cusId = (int) session.getUserProperties().get("instantCus");
                int chatId = (int) session.getUserProperties().get("instantChat");
                List<MessageDTO> lastMessages = messageCache.get(cusId);
                if (lastMessages == null || lastMessages.isEmpty()) {
                    session.getBasicRemote().sendText("No messages available.");
                    return;
                }

                // Lấy thời gian của tin nhắn cuối cùng trong cache
                Timestamp lastMessageTimestamp = lastMessages.get(lastMessages.size() - 1).getDate();

                // Lấy thêm tin nhắn từ service
                List<MessageDTO> moreMessages = messageService.getMessages(chatId, lastMessageTimestamp);
                if (moreMessages.isEmpty()) {
                    session.getBasicRemote().sendText("noMoreMessages");
                } else {
                    // Thêm tin nhắn mới vào cache và gửi lại
                    messageCache.get(cusId).addAll(moreMessages);
                    sendMessagesToSession(cusId, session);
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

    private void sendListCusToAdmin(Session session) throws IOException {
        List<CustomerDTO> customerList = customerService.GetAllCustomer();
        StringBuilder customerListHtml = new StringBuilder();

        if(customerList != null) {
            // Duyệt qua tất cả khách hàng
            for (CustomerDTO customer : customerList) {
                // Xây dựng HTML cho khách hàng với thông báo nếu có tin nhắn chưa xem
                customerListHtml.append("<div class='customer-item' onclick='switchChat(")
                        .append(customer.getUserID())
                        .append(")'><tr>")
                        .append(customer.getFullName());

                // Thêm thông báo "Có tin nhắn chưa xem" nếu có tin nhắn chưa đọc
                if (customer.getLastMessageStatus() != null && customer.getLastMessageStatus() == false) {
                    customerListHtml.append("<span class='new-message-indicator'>có tin nhắn mới</span>");
                }

                customerListHtml.append("</div>");
            }

            // Gửi danh sách khách hàng cho admin
            session.getBasicRemote().sendText(customerListHtml.toString());
        }
    }

    private void loadMessagesForCustomer(Session session, int userId, int chatId) throws IOException, SQLException {

        if (chatId == -1) {
            chatId = chatService.getOrCreateChatId(userId);
        }
        List<MessageDTO> recentMessages = messageCache.get(userId);

        // Nếu có tin nhắn trong bộ nhớ đệm, gửi ngay cho session
        if (recentMessages != null && !recentMessages.isEmpty()) {
            sendMessagesToSession(userId, session);
        } else {
            // Nếu không có tin nhắn, lấy từ cơ sở dữ liệu và lưu vào bộ nhớ đệm
            List<MessageDTO> fetchedMessages = messageService.getRecentMessages(chatId);
            if (fetchedMessages != null && !fetchedMessages.isEmpty()) {
                messageCache.put(userId, fetchedMessages); // Lưu vào bộ nhớ đệm
                sendMessagesToSession(userId, session);
            } else {
                chatSessions.computeIfAbsent(userId, k -> new HashSet<>()).add(session);
                session.getBasicRemote().sendText("No previous messages.");
            }
        }
        session.getUserProperties().put("instantCus", userId);
        session.getUserProperties().put("instantChat", chatId);
    }

    private void switchChat(Session session, int customerId) throws IOException, SQLException {
        List<MessageDTO> messages = messageCache.get(customerId);

        if (messages != null && !messages.isEmpty()) {
            // Nếu có tin nhắn trong bộ nhớ đệm, gửi trực tiếp
            sendMessagesToSession(customerId, session);
        } else {
            loadMessagesForCustomer(session, customerId, -1);
        }
        int chatId = chatService.getOrCreateChatId(customerId);
        session.getUserProperties().put("instantCus", customerId);
        session.getUserProperties().put("instantChat", chatId);
    }

    private void handleMessage(Session session, String messageContent) throws IOException, SQLException {
        int userId = (int) session.getUserProperties().get("instantCus"); // ID của khách hàng
        int userSend = (int) session.getUserProperties().get("userId"); // ID của người gửi tin nhắn
        int chatId = (int) session.getUserProperties().get("instantChat"); // ID cuộc trò chuyện hiện tại

        // Tạo đối tượng MessageDTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatId(chatId);
        messageDTO.setUserId(userSend);
        messageDTO.setContent(messageContent);
        messageDTO.setDate(new Timestamp(System.currentTimeMillis()));
        messageDTO.setRead(false); // Đánh dấu tin nhắn là chưa đọc

        // Lưu tin nhắn vào cache và database
        messageCache.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(0, messageDTO);

        // Phát sóng tin nhắn tới các session liên quan
        broadcastMessages(userId);

//        // Kiểm tra xem admin có đang tham gia cuộc trò chuyện này không
//        Set<Session> adminSessions = chatSessions.get(userId);  // Lấy tất cả sessions liên quan đến chat của khách hàng
//        if (adminSessions != null) {
//            for (Session adminSession : adminSessions) {
//                if (adminSession.isOpen()) {
//                    // Kiểm tra xem admin có đang tham gia vào cuộc trò chuyện này không
//                    Integer currentChatId = (Integer) adminSession.getUserProperties().get("instantChat");
//                    if (currentChatId != null && currentChatId.equals(chatId)) {
//                        messageService.updateMessageStatus(currentChatId, userSend);
//                        sendListCusToAdmin(adminSession);  // Gửi lại danh sách khách hàng có tin nhắn mới
//                    }
//                }
//            }
//        }

        messageService.saveMessage(messageDTO);
    }

    private void broadcastMessages(int userId) throws IOException {
        // Debug: in ra trước khi phát sóng tin nhắn
        System.out.println("Broadcasting messages for chatId: " + userId);

        List<MessageDTO> messages = messageCache.get(userId);
        if (messages == null || messages.isEmpty()) {
            System.out.println("No messages found for userId: " + userId);  // Debug: không có tin nhắn
            return;
        }

        Set<Session> sessions = chatSessions.get(userId);
        if (sessions == null) {
            System.out.println("No sessions found for userId: " + userId);  // Debug: không có session
            return;
        }

        // Gửi tin nhắn tới tất cả các session trong chat
        for (Session s : sessions) {
            if (s.isOpen()) {
                sendMessagesToSession(userId, s);
            }
        }
    }

    private void sendMessagesToSession(int userID, Session session) throws IOException {
        chatSessions.computeIfAbsent(userID, k -> new HashSet<>()).add(session);
        List<MessageDTO> messages = messageCache.get(userID);
        StringBuilder messagesHtml = new StringBuilder();
        String currentDate = ""; // Biến để lưu ngày hiện tại
        MessageDTO lastOpponentMessage = null; // Biến lưu tin nhắn cuối của đối phương

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
            String userName = "";
            UserDTO user = (UserDTO) session.getUserProperties().get("user");
            if ("ADMIN".equals(user.getAccount().getRole().name().toString())) {
                userName = (message.getUserId() == Integer.parseInt(userId)) ? "You" : "Khách hàng" + message.getUserId();
            }
            else{
                userName = (message.getUserId() == Integer.parseInt(userId)) ? "You" : "Chăm Sóc Khách Hàng";
            }


            // Tạo HTML cho từng tin nhắn
            messagesHtml.append("<div class='message ").append(messageClass).append("'>")
                    .append("<b>").append(userName).append(":</b> ")
                    .append(message.getContent())
                    .append("<div class='time'>").append(messageTime).append("</div>")
                    .append("</div>");

            // Nếu tin nhắn là của đối phương, lưu lại tin nhắn cuối của đối phương
            if (message.getUserId() != Integer.parseInt(userId)) {
                lastOpponentMessage = message;
            }
        }

        // Gửi HTML cho tất cả tin nhắn
        if (session.isOpen()) {
            // Gửi HTML cho tất cả tin nhắn
            session.getBasicRemote().sendText(messagesHtml.toString());
        } else {
            System.out.println("Session is closed, cannot send message.");
        }

        // Kiểm tra và cập nhật trạng thái "đã đọc" cho tin nhắn của đối phương (nếu có)
        if (lastOpponentMessage != null && !lastOpponentMessage.isRead()) {
            lastOpponentMessage.setRead(true);
            int userId = Integer.parseInt(session.getUserProperties().get("userId").toString());
            messageService.updateMessageStatus(lastOpponentMessage.getChatId(), userId);
            UserDTO user = (UserDTO) session.getUserProperties().get("user");
            if ("ADMIN".equals(user.getAccount().getRole().name().toString())) {
                sendListCusToAdmin(session);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Xóa session khỏi chatSessions
        chatSessions.values().forEach(sessionSet -> sessionSet.remove(session));

        // Xóa tin nhắn khỏi cache khi session đóng
        Integer userId = (Integer) session.getUserProperties().get("instantCus");
        if (userId != null) {
            messageCache.remove(userId); // Xóa cache của customerId
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}