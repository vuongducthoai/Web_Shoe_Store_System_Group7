package controller.admin;

import dto.OrderDTO;
import enums.OrderStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IOrderService;
import service.Impl.OrderServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/orders/update-status"})
public class UpdateOrderStatusController extends HttpServlet {
    private IOrderService orderService = new OrderServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");  // Lấy orderId từ request
        String newStatus = req.getParameter("newStatus");  // Lấy newStatus từ request

        // Xử lý cập nhật trạng thái đơn hàng
        boolean success = orderService.updateOrderStatus(orderId, OrderStatus.valueOf(newStatus));

        if (success) {
            // Làm mới danh sách fullOrderList trong ServletContext
            synchronized (getServletContext()) {
                List<OrderDTO> updatedOrderList = orderService.findAllOrders();
                getServletContext().setAttribute("fullOrderList", updatedOrderList);
            }
            // Chuyển hướng về danh sách đơn hàng sau khi cập nhật
            resp.sendRedirect(req.getContextPath() + "/admin/orders?message=success");
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?message=fail");
        }
    }
}
