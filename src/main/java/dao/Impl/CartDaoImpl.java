package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICartDao;
import dto.*;
import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements ICartDao {
    @Override
    public List<CartItemDTO> findAll(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<CartItemDTO> cartItems = new ArrayList<>();

        try {
            // Sử dụng JOIN FETCH để tải dữ liệu cần thiết trong một truy vấn
            List<Object[]> results = entityManager.createQuery(
                            "SELECT ci, p, pr " +
                                    "FROM CartItem ci " +
                                    "JOIN FETCH ci.cart c " +
                                    "JOIN FETCH c.customer cu " +
                                    "LEFT JOIN FETCH ci.product p " +
                                    "LEFT JOIN FETCH p.promotion pr " +
                                    "WHERE cu.id = :userID", Object[].class)
                    .setParameter("userID", userID)
                    .getResultList();

            // Map dữ liệu trả về thành CartItemDTO
            for (Object[] result : results) {
                CartItem cartItem = (CartItem) result[0];
                Product product = (Product) result[1];
                Promotion promotion = (Promotion) result[2];

                // Tạo PromotionDTO
                PromotionDTO promotionDTO = promotion != null
                        ? new PromotionDTO(
                        promotion.getPromotionID(),
                        promotion.getPromotionName(),
                        promotion.getStartDate(),
                        promotion.getEndDate(),
                        null,
                        promotion.getDiscountValue(),
                        promotion.getDiscountType(),
                        promotion.getMinimumLoyalty(),
                        promotion.isActive())
                        : null;

                // Tạo ProductDTO
                ProductDTO productDTO = new ProductDTO(
                        product.getProductID(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getImage(),
                        product.getColor(),
                        product.getSize(),
                        product.isStatus(),
                        product.getDescription(),
                        null,
                        null,
                        null,
                        promotionDTO);

                // Tạo CartItemDTO
                CartItemDTO cartItemDTO = new CartItemDTO(
                        cartItem.getCartItemId(),
                        cartItem.getQuantity(),
                        null,
                        productDTO);

                cartItems.add(cartItemDTO);
            }
        } finally {
            entityManager.close();
        }

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
        query.setParameter("size_i",size_i);
        query.setParameter("Color_i",Color_i);
        query.setParameter("status",1);
        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public boolean RemoveItem(int cartItemId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        CartItem cartItem = entityManager.find(CartItem.class, cartItemId);
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

    public boolean AddItem(int idProduct,int idUser) {
        try {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            Cart cart = entityManager.createQuery("select p " +
                    "from Cart p where p.customer.userID = :idUser",Cart.class)
                    .setParameter("idUser",idUser).getSingleResult();
            Product product = entityManager.find(Product.class, idProduct);
            String queryStr = "select p " +
                    "from CartItem p where p.cart = :cartId and p.product.productName like :name and " +
                    "p.product.size = :size_i and p.product.color like :Color_i ";
            Query query = entityManager.createQuery(queryStr);
            query.setParameter("cartId",cart);
            query.setParameter("name",product.getProductName());
            query.setParameter("Color_i",product.getColor());
            query.setParameter("size_i",product.getSize());
            CartItem cartItem = (CartItem) query.getSingleResult();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity()+1);
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
        Customer customer = entityManager.find(Customer.class, userId);
        Cart cart = customer.getCart();
        int idCart = cart.getCartID(); // id cart vua user
        Product product = entityManager.find(Product.class, idProduct);
        String queryStr = "select p " +
                "from CartItem p where p.cart = :cartId and p.product.productName like :name and " +
                "p.product.size = :size_i and p.product.color like :Color_i ";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("cartId",cart);
        query.setParameter("name",product.getProductName());
        query.setParameter("Color_i",product.getColor());
        query.setParameter("size_i",product.getSize());
        CartItem cartItem = (CartItem) query.getSingleResult();
        String queryStr1 = "select count (*)" +
                "from Product p where productName like :name and " +
                "p.size = :size_i and color like :Color_i and status = :status";
        Query query1 = entityManager.createQuery(queryStr1);
        query1.setParameter("name",product.getProductName());
        query1.setParameter("size_i",product.getSize());
        query1.setParameter("Color_i",product.getColor());
        query1.setParameter("status",true);
        Long count = (Long) query1.getSingleResult();
        if (cartItem != null) {
            if (count>=cartItem.getQuantity()+1)
                return true;
            else return false;
        }
        else {
            if (count>0)
                return true;
            else
                return false;
        }
    }

    @Override
    public AddressDTO getAddressUser(int idUser) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        Customer customer = entityManager.find(Customer.class, idUser);
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
        CartItem cartItem = entityManager.find(CartItem.class, cartItemId);
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
