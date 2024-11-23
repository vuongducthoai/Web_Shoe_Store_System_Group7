package dao;

import dto.AddressDTO;
import dto.CartItemDTO;
import dto.OrderDTO;
import enums.OrderStatus;

import java.util.List;

public interface IOrderDao {
    boolean CreateOrder(OrderDTO order, AddressDTO addressDTO);
    boolean CanCreateOrder(List<CartItemDTO> cartItem);
    List<OrderDTO> findAllOrders();
    boolean updateOrderStatus(String orderId, OrderStatus newStatus);
}
