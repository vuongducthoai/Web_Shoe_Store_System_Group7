package dao;

import dto.*;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import entity.Promotion;

import java.util.List;

public interface ICartDao {
    List<CartItem> findAll(int userID);
    int Count_Item(String name,int size,String Color);
    boolean RemoveItem( int cartItemId);
    boolean AddItem(int idProduct,int userId);
    boolean canAdd(int idProduct,int userId);
    boolean canAddQuantity(int idProduct,int userId,int quantity);
    AddressDTO getAddressUser(int idUser);
    boolean deleteCartItem(int cartItemId);
    boolean AddItemWithQuantity(int idProduct,int userId,int quantity);
    int CountQuantity(ProductDTO productDTO);
    int CountQuantityCart(int UserID);
    List<Promotion> GetAllPromotionByLoayti(int idUser);
}
