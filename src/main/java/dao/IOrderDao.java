
package dao;

import dto.CartItemDTO;
import dto.OrderDTO;
import enums.OrderStatus;

import java.util.List;

public interface IOrderDao {
    boolean CreateOrder(OrderDTO order,int amount, int discount, int feeShip,String orderId);
    boolean CanCreateOrder(List<CartItemDTO> cartItem);
    List<OrderDTO> findAllOrders();
    boolean updateOrderStatus(String orderId, OrderStatus newStatus);
    List<OrderDTO> findOrdersByCustomerId(int customerId);
    OrderDTO getOrderById(int orderId);
    long quantityOrderCompleted();
}
