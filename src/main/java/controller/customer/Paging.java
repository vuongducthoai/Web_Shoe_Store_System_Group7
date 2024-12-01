package controller.customer;

import dto.PromotionProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IProductPromotion;
import service.Impl.ProductPromotionImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/Paging")
public class Paging extends HttpServlet {
    private final IProductPromotion productPromotion = new ProductPromotionImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Load Promotion
        LocalDate startDate = LocalDate.parse("2024-01-20");
        LocalDate endDate = LocalDate.parse("2025-12-30");
        int page = 8;
        int index = 1;
        int num = productPromotion.countProductPromotion(startDate, endDate);
        int numpage = num / page;   //So luong trang
        int num2 = num % page;

        if(num != 0 && num2 != 0){
            numpage++;
        }


        try {
            index = Integer.parseInt(req.getParameter("index")); // index = 2
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop8ProductPromotionNow(startDate, endDate, index - 1, page);
        req.setAttribute("promotionProductDTOList", promotionProductDTOList);
        req.setAttribute("index", index);
        req.setAttribute("numpage", numpage);
        req.getRequestDispatcher("/home").forward(req, resp);
    }
}
