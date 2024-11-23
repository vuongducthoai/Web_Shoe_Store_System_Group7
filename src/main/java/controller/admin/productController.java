package controller.admin;

import dao.IProductDAO;
import dao.Impl.ProductDAOImpl;
import dto.AccountDTO;
import dto.CartItemDTO;
import dto.ProductDTO;
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

@WebServlet(urlPatterns = {"/ProductController"})
public class productController extends HttpServlet {
    IProductDAO productDAO = new ProductDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductDTO> products = productDAO.getListProductDTO();
        if (products == null || products.isEmpty()) {
            // Nếu không có sản phẩm, in ra thông báo lỗi
            System.out.println("Error: No products found or retrieval failed.");
        }
        // Truyền danh sách sản phẩm vào request
        req.setAttribute("products", products);

        // Forward request tới JSP
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }


}
