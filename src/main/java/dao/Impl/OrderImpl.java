
package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IOrderDao;
import dto.*;
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
import java.util.stream.Collectors;

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
    public boolean CanCreateOrder(List<CartItemDTO> cartItem){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        for (CartItemDTO item : cartItem) {
            try {
                Product product = entityManager.find(Product.class, item.getProductDTO().getProductId());
                Object[] quantity = entityManager.createQuery("select count (*) from Product p" +
                                " where p.productName like :name and p.color like :color and p.size = :size" +
                                " and p.status = true", Object[].class)
                        .setParameter("name", product.getProductName())
                        .setParameter("color", product.getColor())
                        .setParameter("size", product.getSize()).getSingleResult();
                long quantit = (long) quantity[0];
                if (item.getQuantity() > quantit) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    @Override
    public List<OrderDTO> findAllOrders() {
        try (EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager()) {
            try {
                // Truy vấn tất cả các Order
                List<Order> orders = entityManager.createQuery("SELECT o FROM Order o", Order.class)
                        .getResultList();

                // Chuyển đổi từ Order sang OrderDTO
                List<OrderDTO> orderDTOs = orders.stream().map(order -> {

                    // Chuyển đổi Customer sang CustomerDTO
                    CustomerDTO customerDTO = new CustomerDTO(
                            order.getCustomer().getUserID(),
                            order.getCustomer().getFullName(),
                            order.getCustomer().getPhone(),
                            order.getCustomer().isActive(),
                            order.getCustomer().getDateOfBirth(),
                            order.getCustomer().getLoyalty()
                    );


                    // Chuyển đổi Payment sang PaymentDTO
                    PaymentDTO paymentDTO = new PaymentDTO(
                            order.getPayment().getPaymentId(),
                            null, // Quan hệ vòng lặp OrderDTO, có thể set null
                            order.getPayment().getPaymentMethod(),
                            order.getPayment().getAmount(),
                            order.getPayment().getPaymentDate(),
                            order.getPayment().isStatus(),
                            order.getPayment().getMomoBillId()
                    );

                    // Tạo OrderDTO
                    return new OrderDTO(
                            String.valueOf(order.getOrderId()),  // Convert orderId to String
                            customerDTO,
                            null,
                            paymentDTO,
                            order.getShippingAddress(),
                            order.getOrderStatus(),
                            order.getOrderDate()
                    );
                }).collect(Collectors.toList());

                return orderDTOs;
            } finally {
                entityManager.close();
            }
        }
    }
    @Override
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        try (EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager()) {
            entityManager.getTransaction().begin();

            // Lấy đơn hàng theo orderId
            Order order = entityManager.find(Order.class, orderId);
            if (order != null) {
                // Cập nhật trạng thái đơn hàng
                order.setOrderStatus(newStatus);
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        }
    }
    public List<OrderDTO> findOrdersByCustomerId(int customerId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn các đơn hàng theo customerId
            List<Order> orders = entityManager.createQuery(
                            "SELECT o FROM Order o WHERE o.customer.userID = :userID", Order.class)
                    .setParameter("userID", customerId)
                    .getResultList();

            // Chuyển đổi từ Order sang OrderDTO
            List<OrderDTO> orderDTOs = orders.stream().map(order -> {
                // Chuyển đổi Customer sang CustomerDTO
                CustomerDTO customerDTO = new CustomerDTO(
                        order.getCustomer().getUserID(),
                        order.getCustomer().getFullName(),
                        order.getCustomer().getPhone(),
                        order.getCustomer().isActive(),
                        order.getCustomer().getDateOfBirth(),
                        order.getCustomer().getLoyalty()
                );

                // Chuyển đổi Payment sang PaymentDTO
                PaymentDTO paymentDTO = null;
                if (order.getPayment() != null) {
                    paymentDTO = new PaymentDTO(
                            order.getPayment().getPaymentId(),
                            null, // Tránh vòng lặp lồng nhau
                            order.getPayment().getPaymentMethod(),
                            order.getPayment().getAmount(),
                            order.getPayment().getPaymentDate(),
                            order.getPayment().isStatus(),
                            order.getPayment().getMomoBillId()
                    );
                }

                // Chuyển đổi danh sách OrderItem sang OrderItemDTO
                List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(orderItem -> {
                    // Chuyển đổi Product sang ProductDTO
                    ProductDTO productDTO = new ProductDTO(
                            orderItem.getProduct().getProductID(),
                            orderItem.getProduct().getProductName(),
                            orderItem.getProduct().getPrice(),
                            orderItem.getProduct().getImage(), // Byte array của ảnh
                            orderItem.getProduct().getColor(),
                            orderItem.getProduct().getSize(),
                            orderItem.getProduct().isStatus(),
                            orderItem.getProduct().getDescription(),
                            null, // Không cần danh sách CartItemDTO trong trường hợp này
                            null, // Không cần danh sách OrderItemDTO trong trường hợp này
                            null, // Không cần CategoryDTO
                            null  // Không cần PromotionProductDTO
                    );

                    // Tạo OrderItemDTO
                    return new OrderItemDTO(
                            orderItem.getOrderItemID(),
                            null, // Không cần tham chiếu đến OrderDTO để tránh vòng lặp
                            productDTO,
                            orderItem.getQuantity()
                    );
                }).collect(Collectors.toList());

                // Tạo OrderDTO
                return new OrderDTO(
                        String.valueOf(order.getOrderId()), // Convert orderId to String
                        customerDTO,
                        orderItemDTOs,
                        paymentDTO,
                        order.getShippingAddress(),
                        order.getOrderStatus(),
                        order.getOrderDate()
                );
            }).collect(Collectors.toList());

            return orderDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        } finally {
            entityManager.close();
        }
    }

}