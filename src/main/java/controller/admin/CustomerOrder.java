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
@WebServlet(urlPatterns = {"/customer/orders", "/customer/orderDetails"})
public class CustomerOrder extends HttpServlet {
    private IOrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/customer/orders".equals(path)) {
            int customerId = 48; // Ví dụ, lấy từ session hoặc context xác thực

            // Gọi service để lấy danh sách đơn hàng của khách hàng
            List<OrderDTO> customerOrders = orderService.getOrdersByCustomerId(customerId);
            if (customerOrders != null && !customerOrders.isEmpty()) {
                req.setAttribute("customerOrders", customerOrders);
            } else {
                System.out.println("Lỗi: Không có đơn hàng");
            }

            // Chuyển hướng đến trang hiển thị đơn hàng
            RequestDispatcher dispatcher = req.getRequestDispatcher("/view/customer/orderHistory.jsp");
            dispatcher.forward(req, resp);
        } else if ("/customer/orderDetails".equals(path)) {
            // Lấy ID đơn hàng từ tham số yêu cầu
            String orderIdParam = req.getParameter("id");

            if (orderIdParam != null) {
                int orderId = Integer.parseInt(orderIdParam);

                // Gọi lại service để lấy chi tiết đơn hàng từ database
                OrderDTO orderDetails = orderService.getOrderById(orderId);

                if (orderDetails != null) {
                    // Lưu thông tin chi tiết đơn hàng vào request và chuyển tiếp đến trang chi tiết
                    req.setAttribute("orderDetails", orderDetails);
                    RequestDispatcher dispatcher = req.getRequestDispatcher("/view/customer/orderDetail.jsp");
                    dispatcher.forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID đơn hàng");
            }
        }
    }
}
