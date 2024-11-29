package service;

import dto.CartItemDTO;

import java.util.List;

public interface ICartService {
    List<CartItemDTO> findAll(int idUser);
    boolean RemoveItem(int cartItemId);
    boolean AddItem(int idProduct,int userId);
    double Total_Cart(List<CartItemDTO> cartItem);
    double FeeShip(int idUser);
    boolean deleteCartItem(int cartItemId);
    double CalculateDiscount(List<CartItemDTO> cartItem,int idUser);

}
