package service.Impl;

import JpaConfig.JpaConfig;
import dao.ICartDao;
import dao.Impl.CartDaoImpl;
import dto.*;
import entity.CartItem;
import entity.Promotion;
import entity.PromotionProduct;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import service.ICartService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CartServiceImpl implements ICartService {

    ICartDao cartDao = new CartDaoImpl();

    @Override
    public List<CartItemDTO> findAll(int idUser) {
        List<CartItem> listC = cartDao.findAll(idUser);
        List<CartItemDTO> listDTO = new ArrayList<>();
        for (CartItem item : listC) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setProductDTO(new ProductDTO());
            if (item.getProduct().getPromotionProducts() != null) {
                List<PromotionProductDTO> listPromotion = new ArrayList<>();
                for (PromotionProduct pp : item.getProduct().getPromotionProducts()) {
                    PromotionProductDTO prp = new PromotionProductDTO();
                    prp.setProduct(cartItemDTO.getProductDTO());
                    prp.setPromotion(new PromotionDTO());
                    prp.getPromotion().setStartDate(pp.getPromotion().getStartDate());
                    prp.getPromotion().setEndDate(pp.getPromotion().getEndDate());
                    prp.getPromotion().setDiscountType(pp.getPromotion().getDiscountType());
                    prp.getPromotion().setActive(pp.getPromotion().isActive());
                    prp.getPromotion().setDiscountValue(pp.getPromotion().getDiscountValue());
                    prp.getPromotion().setPromotionType(pp.getPromotion().getPromotionType());
                    prp.getPromotion().setPromotionId(pp.getPromotion().getPromotionID());
                    prp.getPromotion().setPromotionName(pp.getPromotion().getPromotionName());
                    listPromotion.add(prp);
                }
                cartItemDTO.getProductDTO().setPromotionProducts(listPromotion);
                cartItemDTO.getProductDTO().setProductId(item.getProduct().getProductID());
                cartItemDTO.getProductDTO().setProductName(item.getProduct().getProductName());
                cartItemDTO.getProductDTO().setPrice(item.getProduct().getPrice());
                cartItemDTO.getProductDTO().setImage(item.getProduct().getImage());
                cartItemDTO.getProductDTO().setColor(item.getProduct().getColor());
                cartItemDTO.getProductDTO().setSize(item.getProduct().getSize());
                cartItemDTO.getProductDTO().setStatus(item.getProduct().isStatus());
                cartItemDTO.getProductDTO().setDescription(item.getProduct().getDescription());
                cartItemDTO.getProductDTO().setQuantity(cartDao.CountQuantity(cartItemDTO.getProductDTO()));
                cartItemDTO.setQuantity(item.getQuantity());
                cartItemDTO.setCartItemId(item.getCartItemId());
                listDTO.add(cartItemDTO);
            }
        }
        return listDTO;
    }

    @Override
    public boolean RemoveItem(int cartItemId) {
        return cartDao.RemoveItem(cartItemId);
    }

    public boolean AddItem(int idProduct, int userId) {
        if (cartDao.canAdd(idProduct, userId)) {
            System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            return cartDao.AddItem(idProduct, userId);
        } else return false;
    }

    public boolean AddItemWithQuantity(int idProduct, int userId, int quantity) {
        if (cartDao.canAddQuantity(idProduct, userId, quantity)) {
            return cartDao.AddItemWithQuantity(idProduct, userId, quantity);
        } else return false;
    }

    @Override
    public int CountQuantityCartItem(int idUSer) {
        return cartDao.CountQuantityCart(idUSer);
    }

    @Override
    public List<PromotionDTO> GetAllPromotionByLoayti(int idUser) {
        List<Promotion> listPr = cartDao.GetAllPromotionByLoayti(idUser);
        List<PromotionDTO> list = new ArrayList<>();
        for (Promotion pr : listPr) {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setPromotionId(pr.getPromotionID());
            promotionDTO.setPromotionName(pr.getPromotionName());
            promotionDTO.setDiscountType(pr.getDiscountType());
            promotionDTO.setDiscountValue(pr.getDiscountValue());
            list.add(promotionDTO);
        }
        return list;
    }

    public double Total_Cart(List<CartItemDTO> cartItem) {
        double total = 0;
        for (CartItemDTO cartItemDTO : cartItem) {
            total += cartItemDTO.getProductDTO().getPrice() * cartItemDTO.getQuantity();
        }
        return total;
    }

    @Override
    public double FeeShip(int idUser) {
        AddressDTO addressDTO = cartDao.getAddressUser(idUser);
        if (addressDTO != null) {
            if (!Objects.equals(addressDTO.getProvince(), "Thành phố Hồ Chí Minh"))
                return 30000;
        }
        return 0;
    }

    @Override
    public boolean deleteCartItem(int cartItemId) {
        return cartDao.deleteCartItem(cartItemId);
    }

    public double CalculateDiscount(List<CartItemDTO> cartItem, int idUser,int idPromotion) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        int Loyati = 0;
        try {
            Object[] obj = entityManager.createQuery("select c.loyalty From Customer c Where c.userID = :idUser", Object[].class)
                    .setParameter("idUser", idUser).getSingleResult();
            Loyati = (int) obj[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        double total = 0;
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
            try {
                List<PromotionProductDTO> listPP = item.getProductDTO().getPromotionProducts();
                double priceT = item.getProductDTO().getPrice() * item.getQuantity();
                for (PromotionProductDTO pp : listPP) {
                    if (pp.getPromotion().getStartDate().compareTo(date) <= 0 &&
                            pp.getPromotion().getEndDate().compareTo(date) >= 0 &&
                            pp.getPromotion().isActive() && pp.getPromotion().getPromotionType() == PromotionType.VOUCHER_PRODUCT) {
                        if (pp.getPromotion().getDiscountType() == DiscountType.Percentage) {
                            total += priceT * pp.getPromotion().getDiscountValue() / 100;
                            priceT = priceT * (100 - pp.getPromotion().getDiscountValue()) / 100;
                        }
                        if (pp.getPromotion().getDiscountType() == DiscountType.VND) {
                            total += pp.getPromotion().getDiscountValue() * item.getQuantity();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Promotion> promotion = entityManager.createQuery(
                        "select p from Promotion p " +
                                "where :date >= p.startDate and :date <= p.endDate and p.isActive = true and p.promotionType = :type " +
                                "and p.minimumLoyalty<= :loyalty and p.promotionID = :id",
                        Promotion.class)
                .setParameter("date", date) // Bỏ dấu ":" ở đây
                .setParameter("type", PromotionType.VOUCHER_ORDER)
                .setParameter("loyalty", Loyati)
                .setParameter("id", idPromotion)
                .getResultList(); // Nếu kết quả chỉ có một record
        if (!promotion.isEmpty()) {
            Promotion pro = promotion.get(0);
            if (pro.getDiscountType() == DiscountType.Percentage) {
                total += total * pro.getDiscountValue() / 100;
            }
            if (pro.getDiscountType() == DiscountType.VND) {
                total += pro.getDiscountValue();
            }
        }
        return total;
    }
}
