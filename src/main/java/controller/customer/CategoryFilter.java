package controller.customer;

import dto.CategoryDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ICategoryService;
import service.Impl.CategoryServiceImpl;
import java.util.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/loadIndex"})
public class CategoryFilter implements Filter {
    private ICategoryService categoryService = new CategoryServiceImpl();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Lấy session
        HttpSession session = httpRequest.getSession();
        List<CategoryDTO> categoryList = (List<CategoryDTO>) session.getAttribute("categoryList");

        if (categoryList == null) {
            // Nếu danh mục chưa có trong session, truy vấn từ cơ sở dữ liệu
            categoryList = categoryService.listCategory();
            session.setAttribute("categoryList", categoryList);
        }

        // Tiếp tục chuỗi request
        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
