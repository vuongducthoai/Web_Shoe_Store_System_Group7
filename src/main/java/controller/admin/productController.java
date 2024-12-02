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
                        product.setProductName(productName);
                        product.setDescription(productDescription);
                        product.setCategory(selectedCategory);
                        product.setPrice(productPrice);
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

    // Map chứa ID (số thứ tự) -> Tên màu
    Map<Integer, String> colorIdToNameMap = new HashMap<>();
    int id = 1; // Bắt đầu từ 1, hoặc giá trị bạn muốn
    for (String color : colors) {
        colorIdToNameMap.put(id++, color);
    }

    // Map chứa thông tin size và tổng quantity cho từng màu
    Map<Integer, Map<Integer, Integer>> sizeQuantityMap = new HashMap<>();
    for (Map.Entry<Integer, String> entry : colorIdToNameMap.entrySet()) {
        Integer colorId = entry.getKey();
        String colorName = entry.getValue();

        // Lọc sản phẩm theo màu
        List<ProductDTO> productsForColor = products.stream()
                .filter(product -> product.getColor().equals(colorName))
                .collect(Collectors.toList());

        // Tính tổng quantity theo size
        Map<Integer, Integer> sizeQuantity = productsForColor.stream()
                .collect(Collectors.groupingBy(
                        ProductDTO::getSize,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
        sizeQuantityMap.put(colorId, sizeQuantity);
    }

    // Lấy hình ảnh cho từng màu (dựa trên ID)
    Map<Integer, String> colorIdToImageMap = new HashMap<>();
    for (Map.Entry<Integer, String> entry : colorIdToNameMap.entrySet()) {
        Integer colorId = entry.getKey();
        String colorName = entry.getValue();

        products.stream()
                .filter(product -> product.getColor().equals(colorName))
                .findFirst()
                .ifPresent(product -> colorIdToImageMap.put(colorId, product.getBase64Image()));
    }

    // Set dữ liệu vào request
    request.setAttribute("colorIdToNameMap", colorIdToNameMap);
    request.setAttribute("colorIdToImageMap", colorIdToImageMap);
    request.setAttribute("sizeQuantityMap", sizeQuantityMap);

    // Chuyển tiếp đến JSP
    LoadListproduct(request, response);
}





//    protected void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int productID = Integer.parseInt(req.getParameter("edit-productID"));
//        String productName = req.getParameter("edit-productName");
//        double productPrice = Double.parseDouble(req.getParameter("edit-productPrice"));
//
//        String productColor = req.getParameter("edit-productColor");
//        int productSize = Integer.parseInt(req.getParameter("edit-productSize"));
//        String categoryName = req.getParameter("edit-CategoryName");
//        String productDescription = req.getParameter("edit-productDescription");
//
//        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
//        Category selectedCategory = null;
//        for (CategoryDTO category : categoryDTOList) {
//            if (category.getCategoryName().equals(categoryName)) {
//                selectedCategory = new Category(); // Khởi tạo đối tượng Category
//                selectedCategory.setCategoryID(category.getCategoryId());
//                selectedCategory.setCategoryName(category.getCategoryName());
//                break;
//            }
//        }
//        Part filePart = req.getPart("edit-productImage"); // "productImage" là tên của input file
//        byte[] imageBytes = null;
//
//        if (filePart != null && filePart.getSize() > 0) {
//            try (InputStream inputStream = filePart.getInputStream();
//                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//                imageBytes = outputStream.toByteArray(); // Chuyển đổi ảnh thành mảng byte
//            }
//        }
//        else {
//            ProductDTO existingProduct = productDAO.getProductByID(productID); // Lấy sản phẩm hiện tại từ database
//            if (existingProduct != null) {
//                imageBytes = existingProduct.getImage(); // Gán lại ảnh cũ
//            }
//        }
//
//        // Debug - Kiểm tra ảnh
//        System.out.println("Image uploaded: " + (imageBytes != null ? "Yes" : "No"));
//        System.out.println("Product ID: " + req.getParameter("edit-productID"));
//        System.out.println("Product Name: " + req.getParameter("edit-productName"));
//        System.out.println("Product Price: " + req.getParameter("edit-productPrice"));
//        System.out.println("Product Color: " + req.getParameter("edit-productColor"));
//        System.out.println("Product Size: " + req.getParameter("edit-productSize"));
//        System.out.println("Category Name: " + req.getParameter("edit-CategoryName"));
//        System.out.println("Product Description: " + req.getParameter("edit-productDescription"));
//
//
//
//        Product product = new Product();
//        product.setProductID(productID);
//        product.setProductName(productName);
//        product.setPrice(productPrice);
//        product.setImage(imageBytes);
//        product.setColor(productColor);
//        product.setSize(productSize);
//        product.setCategory(selectedCategory);
//        product.setDescription(productDescription);
//        product.setCreateDate(LocalDateTime.now());
//        product.setStatus(true);
//
//        try
//        {
//            if  (productDAO.AddProduct(product))
//            {
//                System.out.println("Update successful");
//                resp.sendRedirect(req.getContextPath() + "/ProductController");
//            }
//            else{
//                System.out.println("Update fail");
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    protected void updateProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. Cập nhật thông tin chung
        String productName = request.getParameter("productName");
        double productPrice = Double.parseDouble(request.getParameter("productPrice"));
        String categoryName = request.getParameter("CategoryName");
        String productDescription = request.getParameter("productDescription");

        // Gọi DAO để cập nhật thông tin sản phẩm
        productDAO.updateProductByCommonInfo(productName, productPrice, categoryName, productDescription);

        // 2. Xử lý danh sách màu
        List<String> oldColors = productDAO.getColorsByProduct(productName); // Lấy danh sách màu cũ từ DB
        List<String> newColors = new ArrayList<>();
        int i = 1; // Khởi tạo chỉ số vòng lặp
        while (true) {
            // Lấy tên màu từ request
            String colorName = request.getParameter("color-name-" + i);

            // Nếu không nhận được giá trị colorName thì thoát khỏi vòng lặp
            if (colorName == null || colorName.isEmpty()) {
                break;
            }
            newColors.add(colorName);
            i++;
        }


            // Xóa màu không còn trong danh sách mới
        for (String oldColor : oldColors) {
            if (!Arrays.asList(newColors).contains(oldColor)) {
                productDAO.deleteProductByColor(productName, oldColor);
            }
        }

        // Thêm màu mới vào danh sách
        for (String newColor : newColors) {
            if (!oldColors.contains(newColor)) {
                addColorWithDetails(request, response, oldColors);
            }
        }

        //Cập nhật thông tin màu cũ

        for (String oldColor : oldColors) {
            int a = 1;
            while (true) {
                String colorName = request.getParameter("color-name-" + a);

                // Nếu không có colorName hoặc colorName là rỗng, thoát khỏi vòng lặp
                if (colorName == null || colorName.isEmpty()) {
                    break;
                }
                if (colorName.equals(oldColor)) {
                    updateOldColor(request, oldColor, productName, a);
                    break; // Không cần kiểm tra thêm nữa nếu đã tìm thấy
                }
                a++;
            }
        }
        LoadListproduct(request, response);

    }

    private void addColorWithDetails(HttpServletRequest req, HttpServletResponse resp, List<String> oldColors) throws Exception {
        String productName = req.getParameter("productName");
        String productDescription = req.getParameter("productDescription");
        String categoryName = req.getParameter("CategoryName");
        double productPrice = Double.parseDouble(req.getParameter("productPrice"));

        // Lấy danh sách danh mục từ database
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
                break;
            }

            // Nếu màu này đã tồn tại trong danh sách cũ thì bỏ qua và tiếp tục vòng lặp
            if (oldColors.contains(colorName)) {
                i++;
                continue;
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
                // Lặp qua các giá trị size và quantity
                for (int j = 0; j < sizes.length; j++) {
                    int size = Integer.parseInt(sizes[j]);
                    int quantity = Integer.parseInt(quantities[j]);

                    for (int k = 0; k < quantity; k++) {
                        // Tạo đối tượng Product và thiết lập các thuộc tính
                        Product product = new Product();
                        product.setProductName(productName);
                        product.setDescription(productDescription);
                        product.setCategory(selectedCategory);
                        product.setPrice(productPrice);
                        product.setColor(colorName);
                        product.setSize(size);
                        product.setImage(imageBytes);
                        product.setCreateDate(LocalDateTime.now());
                        product.setStatus(true);

                        // Thêm sản phẩm vào cơ sở dữ liệu
                        productDAO.AddProduct(product);
                    }
                }
            }

            // Tăng giá trị chỉ số i lên để tiếp tục vòng lặp
            i++;
        }

    }

    private void updateOldColor(HttpServletRequest request, String color, String productName, int a) throws Exception {
        // Cập nhật hình ảnh nếu có thay đổi

        Part filePart = request.getPart("image-color-" + a);
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

        if (productDAO.updateImage(color, productName, imageBytes))
            System.out.println(productName + " has been updated");
        else
            System.out.println(productName + " has not been updated");

        // Lấy danh sách kích thước hiện tại từ DB
        List<Integer> oldSizes = productDAO.getSizesByColor(color, productName);

        // Lấy danh sách kích thước và số lượng mới từ request
        String[] sizeParams = request.getParameterValues("size-" + a + "[]");
        String[] quantityParams = request.getParameterValues("quantity-" + a + "[]");

        if (sizeParams != null && quantityParams != null) {
            Map<Integer, Integer> oldQuantities = productDAO.getQuantitiesByColor(color, productName);

            for (int i = 0; i < sizeParams.length; i++) {
                int newSize = Integer.parseInt(sizeParams[i]);
                int newQuantity = Integer.parseInt(quantityParams[i]);

                if (!oldSizes.contains(newSize)) {

                    // Thêm sản phẩm dựa trên số lượng mới
                    for (int j = 0; j < newQuantity; j++) {
                        addProductBySizeAndQuantity(productName, color, newSize);
                    }
                } else {
                    // Xử lý khác biệt số lượng
                    int oldQuantity = oldQuantities.getOrDefault(newSize, 0);
                    int quantityDifference = newQuantity - oldQuantity;

                    if (quantityDifference > 0) {
                        for (int j = 0; j < quantityDifference; j++) {
                            addProductBySizeAndQuantity(productName, color, newSize);
                        }
                    } else if (quantityDifference < 0) {
                        productDAO.reduceProductInstances(productName, color, newSize, -quantityDifference);
                    }
                }
            }

            // Xóa các kích thước cũ không còn trong danh sách mới
            for (Integer oldSize : oldSizes) {
                if (Arrays.stream(sizeParams).noneMatch(s -> Integer.parseInt(s) == oldSize)) {
                    productDAO.deleteSize(productName, color, oldSize);
                }
            }
        }
    }

    protected void addProductBySizeAndQuantity(String productName, String color, int size) throws ServletException, IOException {
        ProductDTO productDTO = new ProductDTO();
        productDTO = productDAO.getCommonInfoByName(productName);
        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        String categoryName= productDTO.getCategoryDTO().getCategoryName();
        Category selectedCategory = null;
        for (CategoryDTO category : categoryDTOList) {
            if (category.getCategoryName().equals(categoryName)) {
                selectedCategory = new Category();
                selectedCategory.setCategoryID(category.getCategoryId());
                selectedCategory.setCategoryName(category.getCategoryName());
                break;
            }
        }
        Product product = new Product();
        product.setProductName(productName);
        product.setDescription(productDTO.getDescription());
        product.setCategory(selectedCategory);
        product.setPrice(productDTO.getPrice());
        product.setColor(color);
        product.setSize(size);
        product.setImage(productDTO.getImage());
        product.setCreateDate(LocalDateTime.now());
        product.setStatus(true);
        productDAO.AddProduct(product);

    }




}
