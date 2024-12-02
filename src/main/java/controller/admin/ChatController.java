package controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChatDTO;
import dto.CustomerDTO;
import dto.MessageDTO;
import dto.UserDTO;
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
import java.net.URLEncoder;
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
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        // Kiểm tra và chuyển đổi dữ liệu
        if (userDTO != null) {

            // Chuyển đổi đối tượng User thành JSON để truyền qua JSP
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(userDTO);

            req.setAttribute("userJson", userJson);
            req.setAttribute("role", userDTO.getAccount().getRole());

            // Chuyển tiếp đến trang chat.jsp
            RequestDispatcher dispatcher = req.getRequestDispatcher("/view/chat.jsp");
            dispatcher.forward(req, resp);
        } else {
            session.invalidate();
            resp.sendRedirect("/view/login.jsp");
            return;
            }
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters");
    }
}
