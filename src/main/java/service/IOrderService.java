package service;

import dto.CartItemDTO;

import java.util.List;

public interface IOrderService {
    boolean CreateOrder(String Json);
    boolean CanCreateOrder(List<CartItemDTO> cartItem);
}
