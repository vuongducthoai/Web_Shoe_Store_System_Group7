package controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChatDTO;
import dto.CustomerDTO;
import dto.MessageDTO;
import entity.Account;
import entity.Chat;
import entity.User;
import enums.RoleType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import service.IChatService;
import service.ICustomerService;
import service.IMessageService;
import service.Impl.ChatService;
import service.Impl.CustomerServiceImpl;
import service.Impl.MessageService;

import java.io.IOException;
import java.net.http.WebSocket;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@WebServlet("/Chat")
public class ChatController extends HttpServlet {

    private IChatService chatService = new ChatService();
    private ICustomerService customerService = new CustomerServiceImpl();
    private IMessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // http://localhost:8080/JPAExample_war_exploded/Chat?userId=67&accountId=41&role=ADMIN
        // ad 10,8 cus 115,70 cus 67 41
        // Lấy tham số từ URL
        String userIdParam = req.getParameter("userId");
        String accountIdParam = req.getParameter("accountId");
        String roleParam = req.getParameter("role");

        // Kiểm tra và chuyển đổi dữ liệu
        if (userIdParam != null && accountIdParam != null && roleParam != null) {
            int userId = Integer.parseInt(userIdParam);
            int accountId = Integer.parseInt(accountIdParam);
            RoleType role = RoleType.valueOf(roleParam);  // Chuyển đổi thành RoleType từ string

            // Tạo đối tượng Account và User
            Account account = new Account(accountId, "admin@example.com", "password", null, "providerID", role, null);
            User user = new User(userId, "Nguyễn Văn A", null, true, account, null);

            // Chuyển đổi đối tượng User thành JSON để truyền qua JSP
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(user);

            // Đặt thuộc tính để truyền vào JSP
            req.setAttribute("userJson", userJson);
            req.setAttribute("role", role.name());

            // Chuyển tiếp đến trang chat.jsp
            RequestDispatcher dispatcher = req.getRequestDispatcher("/view/chat.jsp");
            dispatcher.forward(req, resp);
        } else {
            // Nếu tham số không hợp lệ, gửi lỗi hoặc trang mặc định
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters");
        }
    }
}
