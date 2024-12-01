package controller.admin;

import com.google.gson.Gson;
import dao.IAccountDAO;
import dao.IProductDAO;
import dao.Impl.AccountDaoImpl;
import dao.Impl.ProductDAOImpl;
import dto.*;
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
import service.IProductPromotion;
import service.IProductService;
import service.IPromotionService;
import service.Impl.CartServiceImpl;
import service.Impl.ProductPromotionImpl;
import service.Impl.ProductServiceImpl;
import service.Impl.PromotionServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@WebServlet(urlPatterns = {"/Admin" ,"/Admin/account"})
public class accountController extends HttpServlet {
    IAccountDAO accountDAO = new AccountDaoImpl();
    IPromotionService promotionService = new PromotionServiceImpl();
    IProductService productService = new ProductServiceImpl();
    IProductPromotion promotionProductService = new ProductPromotionImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy danh sách tài khoản từ DAO
        List<AccountDTO> accounts = accountDAO.getListAccountDTO();

        if (accounts == null || accounts.isEmpty()) {
            // Nếu không có tài khoản, in ra thông báo lỗi
            System.out.println("Error: No accounts found or retrieval failed.");
        }
        List<PromotionDTO> promotionDTOList = promotionService.findAll();
        List<String> nameProductList = productService.getDistinctProductNames();


        req.setAttribute("promotionDTOList", promotionDTOList);
        req.setAttribute("nameProductList", nameProductList);

        // Truyền danh sách tài khoản vào request
        req.setAttribute("accounts", accounts);

        // Forward request tới JSP
        req.getRequestDispatcher("/view/admin/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();
        if ("/Admin".equals(path)) {


            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            // Chuyển đổi chuỗi JSON thành JSONObject
            JSONObject jsonObject = new JSONObject(sb.toString());
            int promotionId = Integer.parseInt(jsonObject.getString("promotionId"));
            List<String> listNameProduct = productService.getListNameProductForPromotion(promotionId);
            Gson gson = new Gson();
            String jsonRespone = gson.toJson(listNameProduct);
            String encodedBase64 = Base64.getEncoder().encodeToString(jsonRespone.getBytes());
            resp.setContentType("application/json");
            resp.getWriter().write(encodedBase64);
        }
        else if("/Admin/account".equals(path)) {



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
            resp.sendRedirect(req.getContextPath() + "/Admin");
        }


    }
}
