package controller.admin;

import dto.CategoryDTO;
import dto.ProductDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.Impl.CategoryServiceImpl;
import java.util.List;

import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/category/list", "/admin/category/insert"})
public class CategoryListController extends HttpServlet {
    ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ProductDTO> categoryList = categoryService.findAll();
        req.setAttribute("categoryList", categoryList);
        RequestDispatcher rq = req.getRequestDispatcher("/views/admin/list-category.jsp");
        rq.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/admin/category/insert".equals(path)) {
            String categoryName = req.getParameter("categoryName");
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryName(categoryName);
            categoryService.insert(categoryDTO);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list"); // Redirect to list after insertion
        }
    }
}
