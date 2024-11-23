package controller.customer;

import dto.CategoryDTO;
import dto.ProductDTO;
import dto.ReviewDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.IProductService;
import service.IReviewService;
import service.Impl.CategoryServiceImpl;
import service.Impl.ProductServiceImpl;
import service.Impl.ReviewServiceImpl;

import java.util.*;

import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final ICategoryService categoryService = new CategoryServiceImpl();
    private final IProductService productService = new ProductServiceImpl();
    private final IReviewService reviewService = new ReviewServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CategoryDTO> categoryDTOList = categoryService.listCategory();
        req.setAttribute("categoryDTOList", categoryDTOList);
        List<ProductDTO> productDTOList = productService.findAllWithPagination(0 ,10);
        req.setAttribute("productDTOList", productDTOList);
        List<ReviewDTO> reviewDTOList = reviewService.getTop5Reviews();
        req.setAttribute("reviewDTOList", reviewDTOList);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
