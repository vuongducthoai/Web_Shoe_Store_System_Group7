package controller.customer;

import dto.CategoryDTO;
import dto.ProductDTO;
import dto.PromotionProductDTO;
import dto.ReviewDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICategoryService;
import service.IProductPromotion;
import service.IProductService;
import service.IReviewService;
import service.Impl.CategoryServiceImpl;
import service.Impl.ProductPromotionImpl;
import service.Impl.ProductServiceImpl;
import service.Impl.ReviewServiceImpl;

import java.time.LocalDate;
import java.util.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/home", "/sign-in", "/sign-up"})
public class HomeController extends HttpServlet {
    private final ICategoryService categoryService = new CategoryServiceImpl();
    private final IProductService productService = new ProductServiceImpl();
    private final IReviewService reviewService = new ReviewServiceImpl();
    private final IProductPromotion productPromotion = new ProductPromotionImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/home")) {
            List<CategoryDTO> categoryDTOList = categoryService.listCategory();
            req.setAttribute("categoryDTOList", categoryDTOList);
            List<ProductDTO> productDTOList = productService.findAllWithPagination(0 ,10);
            req.setAttribute("productDTOList", productDTOList);
            List<ReviewDTO> reviewDTOList = reviewService.getTop5Reviews();
            req.setAttribute("reviewDTOList", reviewDTOList);

//        LocalDate endDate= LocalDate.now();
//        LocalDate startDate  = LocalDate.now().minusDays(7);

            LocalDate startDate = LocalDate.parse("2022-01-06");
            LocalDate endDate = LocalDate.parse("2023-11-25");
            List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop5ProductPromotionNow(startDate, endDate);
            req.setAttribute("promotionProductDTOList", promotionProductDTOList);
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else if(path.equals("/sign-in")) {
            resp.sendRedirect("/view/login.jsp");
        } else if(path.equals("/sign-up")) {
            resp.sendRedirect("/view/Register.jsp");
        }

    }
}
