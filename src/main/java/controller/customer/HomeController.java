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

import java.awt.print.Pageable;
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

            //Load Category
            List<CategoryDTO> categoryDTOList = categoryService.listCategory();
            req.setAttribute("categoryDTOList", categoryDTOList);

            //Load Product
            List<ProductDTO> productDTOList = productService.findAllWithPagination(0 ,8);
            req.setAttribute("productDTOList", productDTOList);


            //Load Review
            List<ReviewDTO> reviewDTOList = reviewService.getTop5Reviews();
            req.setAttribute("reviewDTOList", reviewDTOList);




            //Load Promotion
            LocalDate startDate = LocalDate.parse("2024-01-20");
            LocalDate endDate = LocalDate.parse("2025-12-30");
            int page = 8;
            int index = 1;
            int num = productPromotion.countProductPromotion(startDate, endDate);
            int numpage = num / page;   //So luong trang
            System.out.println("NumPage " + numpage);
            int num2 = num % page;

            if(num != 0 && num2 != 0){
                numpage++;
            }

            int offset = (index - 1) * page;

           List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop8ProductPromotionNow(startDate, endDate, offset, page);
           req.setAttribute("promotionProductDTOList", promotionProductDTOList);
           req.setAttribute("index", index);
           req.setAttribute("numpage", numpage);
            req.getRequestDispatcher("./index.jsp").forward(req, resp);
        } else if(path.equals("/sign-in")) {
            resp.sendRedirect("/view/login.jsp");
        } else if(path.equals("/sign-up")) {
            resp.sendRedirect("/view/Register.jsp");
        }
    }
}
