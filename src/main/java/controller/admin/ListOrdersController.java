package controller.admin;

import dto.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IOrderService;
import service.Impl.OrderServiceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(urlPatterns = {"/admin/orders"})
public class ListOrdersController extends HttpServlet {
    private IOrderService orderService = new OrderServiceImpl();

    @Override
    public void init() throws ServletException {
        super.init();
        synchronized (getServletContext()) {
            if (getServletContext().getAttribute("fullOrderList") == null) {
                List<OrderDTO> orderDTOList = orderService.findAllOrders();
                getServletContext().setAttribute("fullOrderList", orderDTOList);
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy các tham số từ request
        String searchKeyword = req.getParameter("search");
        String orderStatus = req.getParameter("status");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        // Gọi phương thức Service để lấy danh sách đơn hàng đã lọc
        List<OrderDTO> orderDTOList = orderService.getFilteredOrders(searchKeyword, orderStatus, startDate, endDate);

        // Gửi dữ liệu đã lọc đến JSP
        req.setAttribute("orderDTOList", orderDTOList);
        req.setAttribute("searchKeyword", searchKeyword);
        req.setAttribute("status", orderStatus);
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);

        RequestDispatcher rq = req.getRequestDispatcher("/view/admin/orders.jsp");
        rq.forward(req, resp);
    }
}
