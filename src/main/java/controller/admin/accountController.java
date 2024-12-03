package controller.admin;

import dao.IAccountDAO;
import dao.Impl.AccountDaoImpl;
import dto.AccountDTO;
import dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.IAccountService;
import service.Impl.AccountServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/AccountController"})
public class accountController extends HttpServlet {
    private final IAccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AccountDTO> accounts = accountService.getListAccounts();

        if (accounts == null || accounts.isEmpty()) {
            System.out.println("Error: No accounts found or retrieval failed.");
        }

        req.setAttribute("accounts", accounts);
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is missing.");
            return;
        }

        String accountID = req.getParameter("accountID");
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");

        int accountIDInt = 0;
        try {
            accountIDInt = Integer.parseInt(accountID);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "ID tài khoản không hợp lệ.");
            resp.sendRedirect(req.getContextPath() + "/AccountController");
            return;
        }

        switch (action) {
            case "update":
                UserDTO userDTO = new UserDTO();
                userDTO.setFullName(fullName);
                userDTO.setPhone(phone);

                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountID(accountIDInt);
                accountDTO.setEmail(email);
                accountDTO.setPassword(password);
                accountDTO.setUser(userDTO);

                boolean isUpdated = accountService.updateAccount(accountDTO);
                if (isUpdated) {
                    session.setAttribute("successMessage", "Cập nhật thành công tài khoản với ID " + accountID);
                } else {
                    session.setAttribute("errorMessage", "Cập nhật thất bại cho tài khoản với ID " + accountID);
                }
                break;

            case "block":
                boolean isBlocked = accountService.updateAccountStatus(accountIDInt, 1);
                if (isBlocked) {
                    session.setAttribute("successMessage", "Tài khoản đã bị chặn.");
                } else {
                    session.setAttribute("errorMessage", "Chặn tài khoản không thành công.");
                }
                break;

            case "unblock":
                boolean isUnblocked = accountService.updateAccountStatus(accountIDInt, 0);
                if (isUnblocked) {
                    session.setAttribute("successMessage", "Tài khoản đã được bỏ chặn.");
                } else {
                    session.setAttribute("errorMessage", "Bỏ chặn tài khoản không thành công.");
                }
                break;

            default:
                session.setAttribute("errorMessage", "Hành động không hợp lệ.");
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/AccountController");
    }
}