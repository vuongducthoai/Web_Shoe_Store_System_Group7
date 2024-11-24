package controller.admin;

import dao.ICategoryDao;
import dao.IProductDAO;
import dao.Impl.CategoryDaoImpl;
import dao.Impl.ProductDAOImpl;
import dto.AccountDTO;
import dto.CartItemDTO;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;
import entity.Product;
import enums.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import service.ICartService;
import service.Impl.CartServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(urlPatterns = {"/ProductController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class productController extends HttpServlet {
    IProductDAO productDAO = new ProductDAOImpl();
    ICategoryDao categoryDao   = new CategoryDaoImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductDTO> products = productDAO.getListProductDTO();
        if (products == null || products.isEmpty()) {
            // Nếu không có sản phẩm, in ra thông báo lỗi
            System.out.println("Error: No products found or retrieval failed.");
        }
        // Truyền danh sách sản phẩm vào request
        req.setAttribute("products", products);

        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        if (categoryDTOList == null || categoryDTOList.isEmpty()) {
            // Nếu không có sản phẩm, in ra thông báo lỗi
            System.out.println("Error: No products found or retrieval failed.");
        }
        else {
            // Duyệt qua danh sách và in ra thông tin từng đối tượng
            for (CategoryDTO category : categoryDTOList) {
                System.out.println("Category ID: " + category.getCategoryId());
                System.out.println("Category Name: " + category.getCategoryName());
                System.out.println("------------------------------");
            }
        }
        req.setAttribute("CategoryList", categoryDTOList);

        req.getRequestDispatcher("/admin.jsp").forward(req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("submitAction");
        System.out.println("action: " + action);
        try{
            switch (action) {
                case "add-product":
                    addProduct(req, resp);
                    break;
               case "edit-product":
                    updateProduct(req, resp);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    protected void addProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String productName = req.getParameter("productName");
        double productPrice = Double.parseDouble(req.getParameter("productPrice"));


        String productColor = req.getParameter("productColor");
        int productSize = Integer.parseInt(req.getParameter("productSize"));
        String categoryName = req.getParameter("CategoryName");
        String productDescription = req.getParameter("productDescription");

        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        Category selectedCategory = null;
        for (CategoryDTO category : categoryDTOList) {
            if (category.getCategoryName().equals(categoryName)) {
                selectedCategory = new Category(); // Khởi tạo đối tượng Category
                selectedCategory.setCategoryID(category.getCategoryId());
                selectedCategory.setCategoryName(category.getCategoryName());
                break;
            }
        }
        Part filePart = req.getPart("productImage"); // "productImage" là tên của input file
        byte[] imageBytes = null;

        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                imageBytes = outputStream.toByteArray(); // Chuyển đổi ảnh thành mảng byte
            }
        }

        // Debug - Kiểm tra ảnh
        System.out.println("Image uploaded: " + (imageBytes != null ? "Yes" : "No"));
        System.out.println("Product Name: " + req.getParameter("productName"));
        System.out.println("Product Price: " + req.getParameter("productPrice"));
        System.out.println("Product Color: " + req.getParameter("productColor"));
        System.out.println("Product Size: " + req.getParameter("productSize"));
        System.out.println("Category Name: " + req.getParameter("CategoryName"));
        System.out.println("Product Description: " + req.getParameter("productDescription"));



        Product product = new Product();

        product.setProductName(productName);
        product.setPrice(productPrice);
        product.setImage(imageBytes);
        product.setColor(productColor);
        product.setSize(productSize);
        product.setCategory(selectedCategory);
        product.setDescription(productDescription);
        product.setCreateDate(LocalDateTime.now());
        product.setStatus(true);

        try
        {
            if  (productDAO.AddProduct(product))
            {
               System.out.println("Add successful");
                resp.sendRedirect(req.getContextPath() + "/ProductController");
            }
            else{
                System.out.println("Add fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int productID = Integer.parseInt(req.getParameter("edit-productID"));
        String productName = req.getParameter("edit-productName");
        double productPrice = Double.parseDouble(req.getParameter("edit-productPrice"));

        String productColor = req.getParameter("edit-productColor");
        int productSize = Integer.parseInt(req.getParameter("edit-productSize"));
        String categoryName = req.getParameter("edit-CategoryName");
        String productDescription = req.getParameter("edit-productDescription");

        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        Category selectedCategory = null;
        for (CategoryDTO category : categoryDTOList) {
            if (category.getCategoryName().equals(categoryName)) {
                selectedCategory = new Category(); // Khởi tạo đối tượng Category
                selectedCategory.setCategoryID(category.getCategoryId());
                selectedCategory.setCategoryName(category.getCategoryName());
                break;
            }
        }
        Part filePart = req.getPart("edit-productImage"); // "productImage" là tên của input file
        byte[] imageBytes = null;

        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                imageBytes = outputStream.toByteArray(); // Chuyển đổi ảnh thành mảng byte
            }
        }

        // Debug - Kiểm tra ảnh
        System.out.println("Image uploaded: " + (imageBytes != null ? "Yes" : "No"));
        System.out.println("Product ID: " + req.getParameter("edit-productID"));
        System.out.println("Product Name: " + req.getParameter("edit-productName"));
        System.out.println("Product Price: " + req.getParameter("edit-productPrice"));
        System.out.println("Product Color: " + req.getParameter("edit-productColor"));
        System.out.println("Product Size: " + req.getParameter("edit-productSize"));
        System.out.println("Category Name: " + req.getParameter("edit-CategoryName"));
        System.out.println("Product Description: " + req.getParameter("edit-productDescription"));



        Product product = new Product();
        product.setProductID(productID);
        product.setProductName(productName);
        product.setPrice(productPrice);
        product.setImage(imageBytes);
        product.setColor(productColor);
        product.setSize(productSize);
        product.setCategory(selectedCategory);
        product.setDescription(productDescription);
        product.setCreateDate(LocalDateTime.now());
        product.setStatus(true);

        try
        {
            if  (productDAO.AddProduct(product))
            {
                System.out.println("Update successful");
                resp.sendRedirect(req.getContextPath() + "/ProductController");
            }
            else{
                System.out.println("Update fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
