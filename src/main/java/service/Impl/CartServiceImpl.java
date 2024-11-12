package service.Impl;

import dao.ICartDao;
import dao.Impl.CartDaoImpl;
import dto.AddressDTO;
import dto.CartDTO;
import dto.CartItemDTO;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import service.ICartService;

import java.util.List;

public class CartServiceImpl implements ICartService {
    ICartDao cartDao = new CartDaoImpl();
    @Override
    public List<CartItemDTO> findAll(int idUser) {
        return cartDao.findAll(idUser);
    }

    @Override
    public boolean RemoveItem(int cartItemId) {
        return cartDao.RemoveItem(cartItemId);
    }

    public boolean AddItem(int idProduct,int userId){
        if (cartDao.canAdd(idProduct, userId)) {
            return cartDao.AddItem(idProduct, userId);
        }
        else return false;
    }
    public double Total_Cart(List<CartItemDTO> cartItem){
        double total = 0;
        for (CartItemDTO cartItemDTO : cartItem) {
            total+=cartItemDTO.getProductDTO().getPrice()*cartItemDTO.getQuantity();
        }
        return total;
    }
    public double Discount(List<CartItemDTO> cartItem){
        return 0;
    }

    @Override
    public double FeeShip(int idUser) {
        AddressDTO addressDTO = cartDao.getAddressUser(idUser);
        if (addressDTO!=null) {
            if (addressDTO.getCity()!="HCM")
                return 30000;
        }
        return 0;
    }

    @Override
    public boolean deleteCartItem(int cartItemId) {
        return cartDao.deleteCartItem(cartItemId);
    }
}
