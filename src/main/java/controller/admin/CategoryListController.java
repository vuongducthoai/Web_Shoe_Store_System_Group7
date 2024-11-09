package controller.admin;

import entity.Cart;
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

@WebServlet(urlPatterns = {"/admin/category/list"})
public class CategoryListController extends HttpServlet {
    ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cart> cateList = categoryService.findAll();
        req.setAttribute("cateList", cateList);
        RequestDispatcher rq = req.getRequestDispatcher("/views/admin/list-category.jsp");
        rq.forward(req, resp);
    }
}
