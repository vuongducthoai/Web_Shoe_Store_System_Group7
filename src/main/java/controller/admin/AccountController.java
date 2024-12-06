package controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dao.IAccountDAO;
import dao.Impl.AccountDaoImpl;
import dto.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import service.*;
import service.Impl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/Admin" ,"/Admin/account","/Admin/product"})
public class AccountController extends HttpServlet {
    private final IAccountService accountService = new AccountServiceImpl();
    IPromotionService promotionService = new PromotionServiceImpl();
    IProductService productService = new ProductServiceImpl();
    private final IOrderService orderService = new OrderServiceImpl();
    IProductService iProductService = new ProductServiceImpl();
    ICategoryService iCategoryService = new CategoryServiceImpl();
    @Override
    public void init() throws ServletException {
        super.init();
        synchronized (getServletContext()) {
            if (getServletContext().getAttribute("fullOrderList") == null) {
                List<OrderDTO> orderDTOList = orderService.findAllOrders();
                getServletContext().setAttribute("fullOrderList", orderDTOList);
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Lấy dữ liệu từ session
        String productName = (String) session.getAttribute("productName");
        Double productPrice = (Double) session.getAttribute("productPrice");
        String productCategory = (String) session.getAttribute("productCategory");
        String productDescription = (String) session.getAttribute("productDescription");

        Map<Integer, String> colorIdToNameMap = (Map<Integer, String>) session.getAttribute("colorIdToNameMap");
        Map<Integer, String> colorIdToImageMap = (Map<Integer, String>) session.getAttribute("colorIdToImageMap");
        Map<Integer, Integer> sizeQuantityMap = (Map<Integer, Integer>) session.getAttribute("sizeQuantityMap");

        // Xóa khỏi session nếu không còn cần thiết
        session.removeAttribute("productName");
        session.removeAttribute("productPrice");
        session.removeAttribute("productCategory");
        session.removeAttribute("productDescription");
        session.removeAttribute("colorIdToNameMap");
        session.removeAttribute("colorIdToImageMap");
        session.removeAttribute("sizeQuantityMap");


        List<ProductDTO> products = iProductService.getListProductDTO();
        req.setAttribute("products", products);
        List<CategoryDTO> categoryDTOList = iCategoryService.categoryDTOList();
        req.setAttribute("CategoryList", categoryDTOList);

        List<AccountDTO> accounts = accountService.getListAccounts();
        String searchKeyword = req.getParameter("search");
        String orderStatus = req.getParameter("status");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        // Gọi phương thức Service để lấy danh sách đơn hàng đã lọc
        List<OrderDTO> orderDTOList = orderService.getFilteredOrders(searchKeyword, orderStatus, startDate, endDate);

        // Gửi dữ liệu đã lọc đến JSP
        req.setAttribute("orderDTOList", orderDTOList);
        req.setAttribute("searchKeyword", searchKeyword);
        req.setAttribute("status", orderStatus);
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);

        if (accounts == null || accounts.isEmpty()) {
            // Nếu không có tài khoản, in ra thông báo lỗi
            System.out.println("Error: No accounts found or retrieval failed.");
        }
        List<PromotionDTO> promotionDTOList = promotionService.findAll();
        List<String> nameProductList = productService.getDistinctProductNames();
        StatisticsServiceImpl statisticsService = new StatisticsServiceImpl();

        long inventoryQuantity = statisticsService.InventoryQuantity();
        long totalAmount = statisticsService.totalAmount();
        long quantityCompleted=statisticsService.getQuantityCompleted();
        long totalMoth= statisticsService.totalMoth();
        Map<Integer, Map<Integer, Long>> totalRevenueForLastFourYears = statisticsService.totalRevenueForLastFourYears();
        Map<String,Integer>  top10product = statisticsService.top10Product();
        req.setAttribute("totalMoth", totalMoth);
        req.setAttribute("inventoryQuantity", inventoryQuantity);
        req.setAttribute("totalAmount", totalAmount);
        req.setAttribute("quantityCompleted", quantityCompleted);
        req.setAttribute("promotionDTOList", promotionDTOList);
        req.setAttribute("nameProductList", nameProductList);
        // Gửi dữ liệu qua request để forward tới JSP
        req.setAttribute("productName", productName);
        req.setAttribute("productPrice", productPrice);
        req.setAttribute("productCategory", productCategory);
        req.setAttribute("productDescription", productDescription);

        req.setAttribute("colorIdToNameMap", colorIdToNameMap);
        req.setAttribute("colorIdToImageMap", colorIdToImageMap);
        req.setAttribute("sizeQuantityMap", sizeQuantityMap);
        ObjectMapper objectMapper1 = new ObjectMapper();
        String jsonDataTop10= objectMapper1.writeValueAsString(top10product);

        // Chuyển đổi dữ liệu Map thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(totalRevenueForLastFourYears);

        // Truyền dữ liệu vào JSP
        req.setAttribute("dataByYear", jsonData);
        req.setAttribute("top10product", jsonDataTop10);

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

                    boolean isUpdated = accountService.updateAccount(accountDTO);

                    if (isUpdated) {
                        req.setAttribute("successMessage", "Cập nhật thành công tài khoản với ID " + accountID);
                    } else {
                        req.setAttribute("errorMessage", "Cập nhật thất bại cho tài khoản với ID " + accountID);
                    }
                    break;

                case "block":
                    // Chặn tài khoản
                    boolean isBlocked = accountService.updateAccountStatus(accountIDInt, 0);
                    if (isBlocked) {
                        req.setAttribute("successMessage", "Tài khoản đã bị chặn.");
                    } else {
                        req.setAttribute("errorMessage", "Chặn tài khoản không thành công.");
                    }
                    break;

                case "unblock":
                    // Bỏ chặn tài khoản
                    boolean isUnblocked = accountService.updateAccountStatus(accountIDInt, 1);
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