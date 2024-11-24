package controller.admin;

import dao.IAccountDAO;
import dao.IProductDAO;
import dao.Impl.AccountDaoImpl;
import dao.Impl.ProductDAOImpl;
import dto.AccountDTO;
import dto.CartItemDTO;
import dto.ProductDTO;
import dto.UserDTO;
import enums.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import service.ICartService;
import service.Impl.CartServiceImpl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/AccountController"})
public class accountController extends HttpServlet {
    IAccountDAO accountDAO = new AccountDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách tài khoản từ DAO
        List<AccountDTO> accounts = accountDAO.getListAccountDTO();

        if (accounts == null || accounts.isEmpty()) {
            // Nếu không có tài khoản, in ra thông báo lỗi
            System.out.println("Error: No accounts found or retrieval failed.");
        }

        // Truyền danh sách tài khoản vào request
        req.setAttribute("accounts", accounts);

        // Forward request tới JSP
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy tham số action từ form
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing.");
            return;
        }

        // Lấy các tham số khác (accountID, fullName, etc.)
        String accountID = req.getParameter("accountID");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");

        int accountIDInt = 0;
        try {
            accountIDInt = Integer.parseInt(accountID);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account ID format.");
            return;
        }

        // Thực hiện hành động dựa trên action
        switch (action) {
            case "update":
                // Cập nhật tài khoản
                UserDTO userDTO = new UserDTO();
                userDTO.setFullName(fullName);
                userDTO.setPhone(phone);

                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountID(accountIDInt);
                accountDTO.setEmail(email);
                accountDTO.setPassword(password);
                accountDTO.setUser(userDTO);

                boolean isUpdated = accountDAO.updateAccount(accountDTO);

                if (isUpdated) {
                    req.setAttribute("successMessage", "Cập nhật thành công tài khoản với ID " + accountID);
                } else {
                    req.setAttribute("errorMessage", "Cập nhật thất bại cho tài khoản với ID " + accountID);
                }
                break;

            case "block":
                // Chặn tài khoản
                boolean isBlocked = accountDAO.updateAccountActive(accountIDInt, 0);  // 0 là trạng thái bị chặn
                if (isBlocked) {
                    req.setAttribute("successMessage", "Tài khoản đã bị chặn.");
                } else {
                    req.setAttribute("errorMessage", "Chặn tài khoản không thành công.");
                }
                break;

            case "unblock":
                // Bỏ chặn tài khoản
                boolean isUnblocked = accountDAO.updateAccountActive(accountIDInt, 1);  // 1 là trạng thái hoạt động
                if (isUnblocked) {
                    req.setAttribute("successMessage", "Tài khoản đã được bỏ chặn.");
                } else {
                    req.setAttribute("errorMessage", "Bỏ chặn tài khoản không thành công.");
                }
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                return;
        }

        // Redirect về trang quản lý tài khoản
        resp.sendRedirect(req.getContextPath() + "/AccountController");
    }

}
