package service;

import dto.CartDTO;
import dto.CartItemDTO;
import dto.PromotionDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;

import java.util.List;

public interface ICartService {
    List<CartItemDTO> findAll(int idUser);
    boolean RemoveItem(int cartItemId);
    boolean AddItem(int idProduct,int userId);
    double Total_Cart(List<CartItemDTO> cartItem);
    double FeeShip(int idUser);
    boolean deleteCartItem(int cartItemId);
    double CalculateDiscount(List<CartItemDTO> cartItem,int idUser,int idPromotion);
    boolean AddItemWithQuantity(int idProduct,int userId,int quantity);
    int CountQuantityCartItem(int idUSer);
    List<PromotionDTO> GetAllPromotionByLoayti(int idUser);
}
