package controller.admin;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.Impl.StatisticsServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/statistics"})
public class StatisticsController extends HttpServlet {
    StatisticsServiceImpl statisticsService = new StatisticsServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long inventoryQuantity = statisticsService.InventoryQuantity();
        long totalAmount = statisticsService.totalAmount();
        System.out.println(inventoryQuantity);
        System.out.println(totalAmount);


        req.setAttribute("inventoryQuantity", inventoryQuantity);
        req.setAttribute("totalAmount", totalAmount);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/view/admin/statistics.jsp");
        dispatcher.forward(req, resp); // Đảm bảo forward trước khi gửi bất kỳ dữ liệu nào


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
