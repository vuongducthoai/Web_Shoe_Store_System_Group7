package controller.admin;

import dao.ICategoryDao;
import dao.IProductDAO;
import dao.Impl.CategoryDaoImpl;
import dao.Impl.ProductDAOImpl;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/CategoryController", "/CategoryController/ListProduct" })
public class CategoryController extends HttpServlet {
    ICategoryDao categoryDao   = new CategoryDaoImpl();
    IProductDAO productDAO = new ProductDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("submitAction");
        System.out.println("action: " + action);
        try{
            switch (action) {
                case "add-category":
                    addCategory(req, resp);
                    break;
                case "edit-category":
                    updateCategory(req, resp);
                    break;
                case "delete-category":
                    deleteCategory(req, resp);
                    break;
                case "viewProducts":
                    getListProductsInCategory(req, resp);
                    break;
                case "deleteProductFromCategory":
                    deleteProductFromCategory(req, resp);
                    break;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getListProductsInCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryIdStr = req.getParameter("categoryId");
        String categoryName = req.getParameter("categoryName");

        if (categoryIdStr != null) {
            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                System.out.println("categoryID: " + categoryIdStr);

                // Lấy danh sách sản phẩm trong category
                List<ProductDTO> productsInCategory = productDAO.findListProductByCategoryID(categoryId);

                // Danh sách tên sản phẩm duy nhất
                Set<String> distinctNameList = new LinkedHashSet<>();
                // Danh sách chứa các thông tin của sản phẩm đã gộp
                List<Map<String, Object>> groupedProducts = new ArrayList<>();

                // Lặp qua tất cả sản phẩm và thu thập các tên sản phẩm duy nhất
                for (ProductDTO product : productsInCategory) {
                    distinctNameList.add(product.getProductName());
                }

                // Lặp qua từng tên sản phẩm duy nhất
                for (String distinctName : distinctNameList) {
                    List<String> colorsDistinct = new ArrayList<>();
                    List<Integer> sizesDistinct = new ArrayList<>();
                    String representativeImage = ""; // Hình ảnh đại diện
                    double productPrice = 0;
                    int quantity =0;

                    // Lặp qua tất cả sản phẩm và thu thập màu sắc, kích thước, và hình ảnh
                    for (ProductDTO product : productsInCategory) {
                        if (distinctName.equals(product.getProductName())) {
                            if (!colorsDistinct.contains(product.getColor())) {
                                colorsDistinct.add(product.getColor());
                            }
                            if (!sizesDistinct.contains(product.getSize())) {
                                sizesDistinct.add(product.getSize());
                            }
                            if (representativeImage.isEmpty()) {
                                representativeImage = product.getBase64Image(); // Lấy hình ảnh của sản phẩm đầu tiên trong nhóm
                            }
                            if (productPrice == 0)
                            {
                                productPrice = product.getPrice();
                            }
                            quantity++;
                        }
                    }

                    // Tạo một map để lưu thông tin về sản phẩm đã gộp
                    Map<String, Object> productGroup = new HashMap<>();
                    productGroup.put("productName", distinctName);
                    productGroup.put("colorsDistinct", colorsDistinct);
                    productGroup.put("sizesDistinct", sizesDistinct);
                    productGroup.put("representativeImage", representativeImage);
                    productGroup.put("productPrice", productPrice);
                    productGroup.put("productQuantity", quantity);

                    // Thêm thông tin vào danh sách
                    groupedProducts.add(productGroup);
                }

                // Sau khi lặp xong, truyền toàn bộ thông tin vào JSP
                req.setAttribute("categoryName", categoryName);
                req.setAttribute("groupedProducts", groupedProducts);

                // Chuyển hướng tới JSP để hiển thị sản phẩm đã gộp
                req.getRequestDispatcher("/ProductInCategory.jsp").forward(req, resp);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






    protected void addCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryName = req.getParameter("add-categoryName");
        System.out.println("categoryName: " + categoryName);
        Category category = new Category();
        category.setCategoryName(categoryName);
        try
        {
            if  (categoryDao.insert(category))
            {
                System.out.println("Add successful");
                resp.sendRedirect(req.getContextPath() + "/Admin");
            }
            else{
                System.out.println("Add fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryId = Integer.parseInt(req.getParameter("edit-categoryID"));
        String categoryName = req.getParameter("edit-categoryName");
        Category category = new Category();
        category.setCategoryID(categoryId);
        category.setCategoryName(categoryName);

        try
        {
            if  (categoryDao.insert(category))
            {
                System.out.println("Update successful");
                resp.sendRedirect(req.getContextPath() + "/Admin");
            }
            else{
                System.out.println("Update fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryId = Integer.parseInt(req.getParameter("delete-categoryID"));
        try
        {
            if  (categoryDao.remove(categoryId))
            {
                System.out.println("Delete successful");
                resp.sendRedirect(req.getContextPath() + "/Admin");
            }
            else{
                System.out.println("Delete fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProductFromCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter("deleteProduct");

        if (productName != null) {
            try {
                // Gọi hàm để xóa sản phẩm khỏi danh mục
                productDAO.deleteProductFromCategory(productName);
                doGet(req,resp);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}


