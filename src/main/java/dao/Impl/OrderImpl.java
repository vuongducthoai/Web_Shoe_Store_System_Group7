package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IOrderDao;
import dto.OrderDTO;
import dto.OrderItemDTO;
import entity.*;
import enums.OrderStatus;
import enums.PaymentMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderImpl implements IOrderDao {
    public boolean CreateOrder(OrderDTO order){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try{
            List<Order> order1 = entityManager.createQuery("Select o " +
                    "from Order o Where o.payment.momoBillId like :momoID",Order.class)
                    .setParameter("momoID",order.getPayment().getMomoBillId()).getResultList();
            if (order1.isEmpty()) {
                Customer customer = entityManager.find(Customer.class, Integer.valueOf(order.getCustomer().getUserID()));
                List<OrderItemDTO> ListOrderItem = order.getOrderItems();
                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                Order orderEnty = new Order();
                orderEnty.setCustomer(customer);
                orderEnty.setOrderDate(Date.from(Instant.now()));
                orderEnty.setOrderStatus(OrderStatus.WAITING_CONFIRMATION);
                entityManager.persist(orderEnty);
                for (OrderItemDTO item : ListOrderItem) {
                    OrderItem orderItem = new OrderItem();
                    Product product = entityManager.find(Product.class, item.getProductDTO().getProductId());
                    orderItem.setOrder(orderEnty);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    entityManager.persist(orderItem);
                    orderItems.add(orderItem);
                    // Cap nhap lai Cart va Product
                    List<CartItem> listCartItem = entityManager.createQuery("Select c " +
                            "from CartItem c Where c.product.productName like :name " +
                            "and c.product.color like :color and c.product.size = :size " +
                                    "and c.cart.customer.userID = :userId ",CartItem.class)
                            .setParameter("name",product.getProductName())
                            .setParameter("color",product.getColor())
                            .setParameter("size",product.getSize())
                            .setParameter("userId",order.getCustomer().getUserID())
                            .getResultList();
                    List<Product> listProduct = entityManager.createQuery("Select p" +
                            " from Product p " +
                            "Where p.productName like :name and p.size = :size " +
                            "and p.color like :color and p.status = true",Product.class)
                            .setParameter("name",product.getProductName())
                            .setParameter("color",product.getColor())
                            .setParameter("size",product.getSize())
                            .setMaxResults(item.getQuantity())
                            .getResultList();
                    if (listProduct.size()!= item.getQuantity()) {
                        transaction.rollback();
                        return false;
                    }
                    else{
                        for (Product product1 : listProduct) {
                            product1.setStatus(false);
                        }
                    }
                    if (!listCartItem.isEmpty()){
                        CartItem cartItem = listCartItem.get(0);
                        int quantity = cartItem.getQuantity() - item.getQuantity();
                        if (quantity > 0) {
                            cartItem.setQuantity(quantity);
                        }
                        else{
                            entityManager.remove(cartItem);
                        }
                    }
                }

                Payment payment = new Payment();
                payment.setPaymentDate(Date.from(Instant.now()));
                payment.setPaymentMethod(PaymentMethod.MOMO);
                payment.setAmount(order.getPayment().getAmount());
                payment.setStatus(true);
                payment.setMomoBillId(order.getPayment().getMomoBillId());
                entityManager.persist(payment);

                orderEnty.setPayment(payment);
                orderEnty.setOrderItems(orderItems);
                payment.setOrder(orderEnty);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
        return true;
    }
}
