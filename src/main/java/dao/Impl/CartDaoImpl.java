package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICartDao;
import dto.*;
import entity.*;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDaoImpl implements ICartDao {
    @Override
    public List<CartItem> findAll(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        List<CartItem> list = entityManager.createQuery("select c from  CartItem c where c.cart.customer.userID = :uID",CartItem.class)
                .setParameter("uID",userID).getResultList();
        transaction.commit();
        return list;
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
            Long count = (Long) entityManager.createQuery("select count(p) from Product p where " +
                    "p.productName like :name and p.color like :color and p.size = :size " +
                    "and p.status=true")
                            .setParameter("name",cartItem.getProduct().getProductName())
                                    .setParameter("color",cartItem.getProduct().getColor())
                                            .setParameter("size",cartItem.getProduct().getSize())
                                                    .getSingleResult();
            if (count == 0){
                entityManager.remove(cartItem);
            }
            else if (cartItem.getQuantity()-1>count){
                cartItem.setQuantity(count.intValue());
            }
            else
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
            e.printStackTrace();
            return false;
        }
    }
    public boolean AddItemWithQuantity(int idProduct,int userId,int quantity){
        try {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            Cart cart = entityManager.createQuery("select p " +
                            "from Cart p where p.customer.userID = :idUser",Cart.class)
                    .setParameter("idUser",Integer.valueOf(userId)).getSingleResult();
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
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            else {
                CartItem cartItem1 = new CartItem();
                cartItem1.setCart(cart);
                cartItem1.setProduct(product);
                cartItem1.setQuantity(quantity);
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

    @Override
    public int CountQuantity(ProductDTO productDTO) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try{
            long count = (long) entityManager.createQuery("select count (p) from Product p where" +
                    " p.productName like :name and p.color like :color and " +
                    "p.size = :size and p.status = true")
                    .setParameter("name",productDTO.getProductName())
                    .setParameter("color",productDTO.getColor())
                    .setParameter("size",productDTO.getSize())
                    .getSingleResult();
            return (int) count;
        }
        catch (Exception e){
            return 0;
        }
    }

    @Override
    public int CountQuantityCart(int UserID) {
        try{
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            long count = (long) entityManager.createQuery("select sum (p.quantity) from CartItem p " +
                            "where p.cart.customer.userID=:id")
                    .setParameter("id",UserID)
                    .getSingleResult();
            return (int) count;
        }
        catch (Exception e){
            return 0;
        }
    }

    @Override
    public List<Promotion> GetAllPromotionByLoayti(int idUser) {
        try{
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            int loyalty = (int) entityManager.createQuery("select c.loyalty from Customer c where c.userID = :userId")
                    .setParameter("userId",idUser).getSingleResult();
            Date date = Date.from(Instant.now());
            List<Promotion> list = entityManager.createQuery("select pr from Promotion pr where " +
                    "pr.minimumLoyalty<= :loyalty and pr.startDate <= :date and pr.endDate>= :date " +
                            "and pr.promotionType = :type and pr.isActive = true order by pr.minimumLoyalty desc",Promotion.class)
                    .setParameter("loyalty",loyalty)
                    .setParameter("date",date).
                    setParameter("type",PromotionType.VOUCHER_ORDER).getResultList();
            return list;
        }
        catch (Exception e){
            return List.of();
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
    public boolean canAddQuantity(int idProduct,int userId,int quantity){
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
            if (count>=cartItem.getQuantity()+quantity)
                return true;
            else return false;
        }
        else {
            return count >= quantity;
        }
    }
    @Override
    public AddressDTO getAddressUser(int idUser) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        Customer customer = entityManager.find(Customer.class, Integer.valueOf(idUser));
        AddressDTO addressDTO = new AddressDTO();
        if (customer== null) return null;
        if (customer.getAddress() != null) {
            addressDTO.setAddressID(customer.getAddress().getAddressID());
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
