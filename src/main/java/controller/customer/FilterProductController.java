package controller.customer;
import dto.CategoryDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.Impl.CategoryServiceImpl;

import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/customer/product/filter"})
public class FilterProductController extends HttpServlet {
    ICategoryService categoryService = new CategoryServiceImpl();
    List<CategoryDTO> categories = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParam = req.getParameter("page");

        if (pageParam == null || pageParam.isEmpty()) {
            String contextPath = req.getContextPath(); // Lấy context path
            String redirectURL = contextPath + "/customer/product/filter?page=1";
            resp.sendRedirect(redirectURL);
            return;
        }

//        if (categories == null) {
            categories = categoryService.findAllCategories();
//            categoryDTOList = new ArrayList<>();
//        }

        Map<String, Object> responseData = categoryService.getFilteredAndSortedProducts(
                categories,
                req.getParameter("selectedPromotion"),
                req.getParameter("selectedCategory"),
                req.getParameter("selectedSize"),
                req.getParameter("selectedColor"),
                req.getParameter("searchName"),
                req.getParameter("minPrice"),
                req.getParameter("maxPrice"),
                req.getParameter("sortOption"),
                pageParam
        );

        for (Map.Entry<String, Object> entry : responseData.entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }

        RequestDispatcher rq = req.getRequestDispatcher("/view/customer/filter-product.jsp");
        rq.forward(req, resp);
    }
}
