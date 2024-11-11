package dao;

import dto.*;
import entity.Cart;
import entity.CartItem;
import entity.Customer;

import java.util.List;

public interface ICartDao {
    List<CartItemDTO> findAll(int userID);
    int Count_Item(String name,int size,String Color);
    boolean RemoveItem( int cartItemId);
    boolean AddItem(int idProduct,int userId);
    boolean canAdd(int idProduct,int userId);
    AddressDTO getAddressUser(int idUser);
    boolean deleteCartItem(int cartItemId);
}
