package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICartDao;
import dto.*;
import entity.*;
import enums.DiscountType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDaoImpl implements ICartDao {
    @Override
    public List<CartItemDTO> findAll(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<CartItemDTO> cartItems = new ArrayList<>();
        try {
            List<Object[]> cartItem1 = entityManager.createQuery(
                            "select distinct c.cartItemId,c.quantity," +
                                    "p.id,p.productName,p.color,p.price,p.image,p.status,p.size,p.description," +
                                    "pr.id,pr.promotionName,pr.startDate,pr.endDate,pr.discountValue,pr.discountType,pr.minimumLoyalty,pr.isActive,pr.promotionType "+
                                    "from CartItem c join Product p on c.product.id = p.id left join Promotion pr on pr.id = p.promotion.id " +
                                    "where c.cart.customer.userID = :userId",Object[].class)
                    .setParameter("userId",Integer.valueOf(userID)).getResultList();
            for (Object[] row : cartItem1) {
                int cartItemId = (int) row[0];
                int quantity = (int) row[1];
                int productId = (int) row[2];
                String productName = (String) row[3];
                String productColor = (String) row[4];
                double productPrice = (double) row[5];
                byte[] productImage = (byte[]) row[6];
                Boolean productStatus = (Boolean) row[7];
                int productSize = (int) row[8];
                String productDescription = (String) row[9];
                PromotionDTO promotionDTO = new PromotionDTO();
                if (row[10] != null) {
                    int promotionId = (Integer) row[10];
                    String promotionName = (String) row[11];
                    Date promotionStartDate = (Date) row[12];
                    Date promotionEndDate = (Date) row[13];
                    Double promotionDiscountValue = (Double) row[14];
                    String promotionDiscountType = (String) row[15];
                    Integer promotionMinimumLoyalty = (Integer) row[16];
                    Boolean promotionIsActive = (Boolean) row[17];
                    String promotionType = (String) row[18];
                    promotionDTO.setPromotionId(promotionId);
                    promotionDTO.setPromotionName(promotionName);
                    promotionDTO.setStartDate(promotionStartDate);
                    promotionDTO.setEndDate(promotionEndDate);
                    promotionDTO.setDiscountValue(promotionDiscountValue);
                    promotionDTO.setDiscountType(promotionDiscountType);
                    promotionDTO.setMinimumLoyalty(promotionMinimumLoyalty);
                    promotionDTO.setActive(promotionIsActive);
                    promotionDTO.setPromotionType(promotionType);
                }
                ProductDTO productDTO = new ProductDTO(
                        productId,
                        productName,
                        productPrice,
                        productImage,
                        productColor,
                        productSize,
                        productStatus,
                        productDescription,
                        null,
                        null,
                        null,
                        promotionDTO);
                CartItemDTO cartItemDTO = new CartItemDTO(
                        cartItemId,
                        quantity,
                        null,
                        productDTO);
                cartItems.add(cartItemDTO);
            }
        } finally {
            entityManager.close();
        }
        System.out.println(cartItems.size());
        return cartItems;
    }

    @Override
    public int Count_Item(String name, int size_i, String Color_i) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        String queryStr = "select count(p) " +
                "from Product p where p.productName like :name and " +
                "p.size = :size_i and p.color = :Color_i and p.status = :status";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("name",name);
        query.setParameter("size_i",Integer.valueOf(size_i));
        query.setParameter("Color_i",Color_i);
        query.setParameter("status",Boolean.valueOf(true));
        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public boolean RemoveItem(int cartItemId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        CartItem cartItem = entityManager.find(CartItem.class, Integer.valueOf(cartItemId));
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        if (cartItem.getQuantity()<=1){
            entityManager.remove(cartItem);
        }
        else{
            cartItem.setQuantity(cartItem.getQuantity()-1);
        }
        transaction.commit();
        return true;
    }

    public boolean AddItem(int idProduct, int idUser) {
        try {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            Cart cart = entityManager.createQuery("select p " +
                            "from Cart p where p.customer.userID = :idUser",Cart.class)
                    .setParameter("idUser",Integer.valueOf(idUser)).getSingleResult();
            Product product = entityManager.find(Product.class, Integer.valueOf(idProduct));
            String queryStr = "select p " +
                    "from CartItem p where p.cart = :cartId and p.product.productName like :name and " +
                    "p.product.size = :size_i and p.product.color like :Color_i ";
            Query query = entityManager.createQuery(queryStr,CartItem.class);
            query.setParameter("cartId",cart);
            query.setParameter("name",product.getProductName());
            query.setParameter("Color_i",product.getColor());
            query.setParameter("size_i",Integer.valueOf(product.getSize()));
            List<CartItem> cartItemL =  query.getResultList();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            if (!cartItemL.isEmpty()) {
                CartItem cartItem = cartItemL.get(0); // Lấy đối tượng đầu tiên
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            }
            else {
                CartItem cartItem1 = new CartItem();
                cartItem1.setCart(cart);
                cartItem1.setProduct(product);
                cartItem1.setQuantity(1);
                entityManager.persist(cartItem1);
                cart.getCardItems().add(cartItem1);
            }
            transaction.commit();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean canAdd(int idProduct,int userId){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        Customer customer = entityManager.find(Customer.class, Integer.valueOf(userId));
        Cart cart = customer.getCart();
        Product product = entityManager.find(Product.class, Integer.valueOf(idProduct));
        String queryStr = "select p " +
                "from CartItem p where p.cart = :cartId and p.product.productName like :name and " +
                "p.product.size = :size_i and p.product.color like :Color_i ";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("cartId",cart);
        query.setParameter("name",product.getProductName());
        query.setParameter("Color_i",product.getColor());
        query.setParameter("size_i",Integer.valueOf(product.getSize()));
        List<CartItem> cartItemL = query.getResultList();
        String queryStr1 = "select count (*)" +
                "from Product p where productName like :name and " +
                "p.size = :size_i and color like :Color_i and status = :status";
        Query query1 = entityManager.createQuery(queryStr1);
        query1.setParameter("name",product.getProductName());
        query1.setParameter("size_i",Integer.valueOf(product.getSize()));
        query1.setParameter("Color_i",product.getColor());
        query1.setParameter("status",Boolean.valueOf(true));
        Long count = (Long) query1.getSingleResult();
        if (!cartItemL.isEmpty()) {
            CartItem cartItem = cartItemL.get(0);
            if (count>=cartItem.getQuantity()+1)
                return true;
            else return false;
        }
        else {
            return count > 0;
        }
    }

    @Override
    public AddressDTO getAddressUser(int idUser) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        Customer customer = entityManager.find(Customer.class, Integer.valueOf(idUser));
        AddressDTO addressDTO = new AddressDTO();
        if (customer.getAddress() != null) {
            addressDTO.setId(customer.getAddress().getId());
            addressDTO.setCity(customer.getAddress().getCity());
            addressDTO.setDistrict(customer.getAddress().getDistrict());
            addressDTO.setProvince(customer.getAddress().getProvince());
            addressDTO.setStreetName(customer.getAddress().getStreetName());
            addressDTO.setHouseNumber(customer.getAddress().getHouseNumber());
            return addressDTO;
        }
        else return null;
    }
    public boolean deleteCartItem(int cartItemId){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        CartItem cartItem = entityManager.find(CartItem.class, Integer.valueOf(cartItemId));
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(cartItem);
            transaction.commit();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
