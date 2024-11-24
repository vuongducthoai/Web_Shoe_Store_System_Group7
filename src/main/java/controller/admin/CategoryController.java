package controller.admin;

import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import dto.CategoryDTO;
import entity.Category;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(urlPatterns = {"/CategoryController"})
public class CategoryController extends HttpServlet {
    ICategoryDao categoryDao   = new CategoryDaoImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CategoryDTO> categoryDTOList = categoryDao.categoryDTOList();
        if (categoryDTOList == null || categoryDTOList.isEmpty()) {
            // Nếu không có sản phẩm, in ra thông báo lỗi
            System.out.println("Error: No products found or retrieval failed.");
        }
        req.setAttribute("categoryList", categoryDTOList);
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

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

            }
        }
        catch (Exception e){
            e.printStackTrace();
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
                resp.sendRedirect(req.getContextPath() + "/CategoryController");
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
                resp.sendRedirect(req.getContextPath() + "/CategoryController");
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
                resp.sendRedirect(req.getContextPath() + "/CategoryController");
            }
            else{
                System.out.println("Delete fail");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


