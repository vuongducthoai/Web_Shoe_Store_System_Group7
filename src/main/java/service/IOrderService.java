package service;

import dto.CartItemDTO;
import dto.OrderDTO;
import enums.OrderStatus;

import java.util.List;

public interface IOrderService {
    boolean CreateOrder(String Json);
    boolean CanCreateOrder(List<CartItemDTO> cartItem);
    List<OrderDTO> findAllOrders();
    boolean updateOrderStatus(String orderId, OrderStatus newStatus);
    List<OrderDTO> getFilteredOrders(String searchKeyword, String orderStatus, String startDate, String endDate);
    List<OrderDTO> getOrdersByCustomerId(int customerId);
}
