package controller.admin;

import dto.OrderDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IOrderService;
import service.Impl.OrderServiceImpl;

import java.io.IOException;
import java.util.List;
@WebServlet(urlPatterns = {"/customer/orders"})
public class CustomerOrder extends HttpServlet {
    private IOrderService orderService = new OrderServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Replace this with the actual customer ID you want to use
        int customerId = 48; // For example, you can get this from session or authentication context

        // Fetch orders for the customer
        List<OrderDTO> customerOrders = orderService.getOrdersByCustomerId(customerId);

        // Set the order list to the request attribute to forward it to the JSP
        req.setAttribute("customerOrders", customerOrders);

        // Forward to the JSP page to display the orders
        RequestDispatcher dispatcher = req.getRequestDispatcher("/view/customer/orderHistory.jsp");
        dispatcher.forward(req, resp);
    }
}
