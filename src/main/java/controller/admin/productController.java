package controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import service.ICartService;
import service.Impl.CartServiceImpl;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
       LoadListproduct(req, resp);
    }


    protected void LoadListproduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            System.out.println("Error: No categorys found or retrieval failed.");
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
                case "showInfo":
                    showInfo(req, resp);
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
        String productDescription = req.getParameter("productDescription");
        String categoryName = req.getParameter("CategoryName");
        double productPrice = Double.parseDouble(req.getParameter("productPrice"));
        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        Category selectedCategory = null;
        for (CategoryDTO category : categoryDTOList) {
            if (category.getCategoryName().equals(categoryName)) {
                selectedCategory = new Category();
                selectedCategory.setCategoryID(category.getCategoryId());
                selectedCategory.setCategoryName(category.getCategoryName());
                break;
            }
        }



        int i = 1; // Khởi tạo chỉ số vòng lặp
        while (true) {
            // Lấy tên màu từ request
            String colorName = req.getParameter("color-name-" + i);

            // Nếu không nhận được giá trị colorName thì thoát khỏi vòng lặp
            if (colorName == null || colorName.isEmpty()) {
                break; // Thoát vòng lặp nếu không có colorName
            }

            // Lấy phần ảnh (file) từ request
            Part filePart = req.getPart("image-color-" + i);
            byte[] imageBytes = null;

            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream();
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    imageBytes = outputStream.toByteArray();
                } catch (IOException e) {
                    // Xử lý lỗi nếu có
                    e.printStackTrace();
                }
            }

            // Lấy các giá trị size và quantity từ request
            String[] sizes = req.getParameterValues("size-" + i + "[]");
            String[] quantities = req.getParameterValues("quantity-" + i + "[]");

            if (sizes != null && quantities != null) {
                // Kiểm tra nếu cả hai mảng đều không rỗng
                for (int j = 0; j < sizes.length; j++) {
                    System.out.println("Size: " + sizes[j] + ", Quantity: " + quantities[j]);
                }

                // Lặp qua các giá trị size và quantity
                for (int j = 0; j < sizes.length; j++) {
                    int size = Integer.parseInt(sizes[j]);
                    int quantity = Integer.parseInt(quantities[j]);

                    for (int k = 0; k < quantity; k++) {
                        Product product = new Product();
                        product.setProductName(productName); // Tham số này bạn cần định nghĩa
                        product.setDescription(productDescription); // Tham số này bạn cần định nghĩa
                        product.setCategory(selectedCategory); // Tham số này bạn cần định nghĩa
                        product.setPrice(productPrice); // Tham số này bạn cần định nghĩa
                        product.setColor(colorName);
                        product.setSize(size);
                        product.setImage(imageBytes);
                        product.setCreateDate(LocalDateTime.now());
                        product.setStatus(true);

                        productDAO.AddProduct(product);
                    }
                }
            }

            // Tăng giá trị chỉ số i lên để tiếp tục vòng lặp
            i++;
        }

        resp.sendRedirect(req.getContextPath() + "/ProductController");
    }


        protected void showInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String productName = request.getParameter("productName");
            List<ProductDTO> products = productDAO.getListProductByName(productName);

            if (products == null || products.isEmpty()) {
                request.setAttribute("error", "Không tìm thấy sản phẩm nào!");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            ProductDTO firstProduct = products.get(0);
            request.setAttribute("productName", firstProduct.getProductName());
            request.setAttribute("productPrice", firstProduct.getPrice());
            request.setAttribute("productCategory", firstProduct.getCategoryDTO().getCategoryName());
            request.setAttribute("productDescription", firstProduct.getDescription());


            // Lấy danh sách màu sắc duy nhất
            List<String> colors = products.stream()
                    .map(ProductDTO::getColor)
                    .distinct()
                    .collect(Collectors.toList());

            // Map chứa thông tin size và tổng quantity cho từng màu
            Map<String, Map<Integer, Integer>> sizeQuantityMap = new HashMap<>();

            for (String color : colors) {
                // Lọc sản phẩm theo màu
                List<ProductDTO> productsForColor = products.stream()
                        .filter(product -> product.getColor().equals(color))
                        .collect(Collectors.toList());

                // Tính tổng quantity theo size
                Map<Integer, Integer> sizeQuantity = productsForColor.stream()
                        .collect(Collectors.groupingBy(
                                ProductDTO::getSize,          // Nhóm theo size
                                Collectors.collectingAndThen(
                                        Collectors.counting(),   // Đếm số lượng sản phẩm cho mỗi size
                                        Long::intValue            // Chuyển đổi từ long sang int
                                )
                        ));

                sizeQuantityMap.put(color, sizeQuantity);

            }

            // Lấy hình ảnh cho từng màu
            Map<String, String> colorImageMap = new HashMap<>();
            for (String color : colors) {
                products.stream()
                        .filter(product -> product.getColor().equals(color))
                        .findFirst()
                        .ifPresent(product -> colorImageMap.put(color, product.getImageBase64()));
            }

            // Set dữ liệu vào request
            request.setAttribute("colors", colors);
            request.setAttribute("colorImageMap", colorImageMap);
            request.setAttribute("sizeQuantityMap", sizeQuantityMap);

            // Chuyển tiếp đến JSP
            LoadListproduct(request, response);
            //request.getRequestDispatcher("admin.jsp").forward(request, response);
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
        else {
            ProductDTO existingProduct = productDAO.getProductByID(productID); // Lấy sản phẩm hiện tại từ database
            if (existingProduct != null) {
                imageBytes = existingProduct.getImage(); // Gán lại ảnh cũ
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
