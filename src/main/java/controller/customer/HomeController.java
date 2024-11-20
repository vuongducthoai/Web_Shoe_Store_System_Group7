package controller.customer;

import dto.CategoryDTO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.IProductService;
import service.Impl.CategoryServiceImpl;
import service.Impl.ProductServiceImpl;

import java.util.*;

import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final ICategoryService categoryService = new CategoryServiceImpl();
    private final IProductService productService = new ProductServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CategoryDTO> categoryDTOList = categoryService.listCategory();
        req.setAttribute("categoryDTOList", categoryDTOList);
        List<ProductDTO> productDTOList = productService.findAllWithPagination(0 ,8);
        req.setAttribute("productDTOList", productDTOList);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
