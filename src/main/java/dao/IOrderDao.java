package dao;

import dto.CartItemDTO;
import dto.OrderDTO;

import java.util.List;

public interface IOrderDao {
    boolean CreateOrder(OrderDTO order);
    boolean CanCreateOrder(List<CartItemDTO> cartItem);
}
