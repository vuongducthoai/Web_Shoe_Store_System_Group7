package service.Impl;

import JpaConfig.JpaConfig;
import dao.ICartDao;
import dao.Impl.CartDaoImpl;
import dto.AddressDTO;
import dto.CartItemDTO;
import entity.Promotion;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import service.ICartService;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Override
    public double FeeShip(int idUser) {
        AddressDTO addressDTO = cartDao.getAddressUser(idUser);
        if (addressDTO!=null) {
            if (!Objects.equals(addressDTO.getProvince(), "Thành phố Hồ Chí Minh"))
                return 30000;
        }
        return 0;
    }

    @Override
    public boolean deleteCartItem(int cartItemId) {
        return cartDao.deleteCartItem(cartItemId);
    }
    public double CalculateDiscount(List<CartItemDTO> cartItem,int idUser){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        int Loyati = 0;
        try{
            Object[] obj = entityManager.createQuery("select c.loyalty From Customer c Where c.userID = :idUser", Object[].class)
                    .setParameter("idUser",idUser).getSingleResult();
            Loyati = (int)obj[0];
        }
        catch(Exception e){
            e.printStackTrace();
        }
        double total=0;
        Date date = Date.from(Instant.now());
        for (CartItemDTO item : cartItem) {
//            try{
//                if (    item.getProductDTO().getPromotionDTO().getStartDate().compareTo(date) <= 0 &&
//                        item.getProductDTO().getPromotionDTO().getEndDate().compareTo(date) >= 0 &&
//                        item.getProductDTO().getPromotionDTO().isActive()) {
//                    if (item.getProductDTO().getPromotionDTO().getDiscountType().equals(DiscountType.Percentage)) {
//                        total += item.getProductDTO().getPromotionDTO().getDiscountValue() *
//                                item.getProductDTO().getPrice() * item.getQuantity() / 100;
//                    }
//                    if (item.getProductDTO().getPromotionDTO().getDiscountType().equals(DiscountType.VND)){
//                        total += item.getProductDTO().getPromotionDTO().getDiscountValue()*item.getQuantity();
//                    }
//                }
//            }catch (Exception ignored){}
        }
        List<Promotion> promotion = entityManager.createQuery(
                        "select p from Promotion p " +
                                "where :date >= p.startDate and :date <= p.endDate and p.isActive = true and p.promotionType = :type " +
                                "and p.minimumLoyalty<= :loyalty order by p.minimumLoyalty asc",
                        Promotion.class)
                .setParameter("date", date) // Bỏ dấu ":" ở đây
                .setParameter("type", PromotionType.VOUCHER_ORDER)
                .setParameter("loyalty", Loyati)
                .setMaxResults(1)
                .getResultList(); // Nếu kết quả chỉ có một record
        if (!promotion.isEmpty()){
            Promotion pro = promotion.get(0);
            if (pro.getDiscountType()==DiscountType.Percentage){
                total += total*pro.getDiscountValue()/100;
            }
            if (pro.getDiscountType()==DiscountType.VND){
                total += pro.getDiscountValue();
            }
        }
        return total;
    }
}
