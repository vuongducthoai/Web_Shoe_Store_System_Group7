
package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IOrderDao;
import dto.*;
import entity.*;
import enums.AuthProvider;
import enums.OrderStatus;
import enums.PaymentMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import service.GmailService;
import service.Impl.GmailServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class OrderImpl implements IOrderDao {
    GmailService gmail_Service = new GmailServiceImpl();

    public boolean CreateOrder(OrderDTO order, int amount, int discount, int feeShip, String orderId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            List<Order> order1 = entityManager.createQuery("Select o " +
                            "from Order o Where o.payment.momoBillId like :momoID", Order.class)
                    .setParameter("momoID", order.getPayment().getMomoBillId()).getResultList();
            if (order1.isEmpty()) {
                Customer customer = entityManager.find(Customer.class, Integer.valueOf(order.getCustomer().getUserID()));
                List<OrderItemDTO> ListOrderItem = order.getOrderItems();
                List<OrderItem> orderItems = new ArrayList<OrderItem>();
                String address = "";
                if (customer.getAddress() != null) {
                    address += customer.getAddress().getHouseNumber() + ", ";
                    address += customer.getAddress().getStreetName() + ", ";
                    address += customer.getAddress().getCity() + ", ";
                    address += customer.getAddress().getDistrict() + ", ";
                    address += customer.getAddress().getProvince();
                }
                Order orderEnty = new Order();
                orderEnty.setCustomer(customer);
                orderEnty.setOrderDate(Date.from(Instant.now()));
                orderEnty.setOrderStatus(OrderStatus.WAITING_CONFIRMATION);
                orderEnty.setShippingAddress(address);
                entityManager.persist(orderEnty);
                for (OrderItemDTO item : ListOrderItem) {
                    OrderItem orderItem = new OrderItem();
                    Product productTWP = entityManager.find(Product.class, item.getProductDTO().getProductId());
                    //
                    item.setProductDTO(new ProductDTO());
                    item.getProductDTO().setProductName(productTWP.getProductName());
                    item.getProductDTO().setSize(productTWP.getSize());
                    item.getProductDTO().setColor(productTWP.getColor());
                    item.getProductDTO().setPrice(productTWP.getPrice());
                    List<CartItem> listCartItem = entityManager.createQuery("Select c " +
                                    "from CartItem c Where c.product.productName like :name " +
                                    "and c.product.color like :color and c.product.size = :size " +
                                    "and c.cart.customer.userID = :userId ", CartItem.class)
                            .setParameter("name", productTWP.getProductName())
                            .setParameter("color", productTWP.getColor())
                            .setParameter("size", productTWP.getSize())
                            .setParameter("userId", order.getCustomer().getUserID())
                            .getResultList();
                    List<Product> listProduct = entityManager.createQuery("Select p" +
                                    " from Product p " +
                                    "Where p.productName like :name and p.size = :size " +
                                    "and p.color like :color and p.status = true", Product.class)
                            .setParameter("name", productTWP.getProductName())
                            .setParameter("color", productTWP.getColor())
                            .setParameter("size", productTWP.getSize())
                            .setMaxResults(item.getQuantity())
                            .getResultList();
                    if (listProduct.size() != item.getQuantity()) {
                        transaction.rollback();
                        return false;
                    } else {
                        for (Product product1 : listProduct) {
                            product1.setStatus(false);
                        }
                    }
                    if (!listCartItem.isEmpty()) {
                        CartItem cartItem = listCartItem.get(0);
                        int quantity = cartItem.getQuantity() - item.getQuantity();
                        if (quantity > 0) {
                            cartItem.setQuantity(quantity);
                        } else {
                            entityManager.remove(cartItem);
                        }
                    }
                    Product product;
                    try {
                        product = entityManager.createQuery(
                                        "select p from Product p " +
                                                "where p.color like :color and p.size = :size " +
                                                "and p.productName like :name and p.status = false " +
                                                "and p.id not in (select o.product.id from OrderItem o " +
                                                "                 where o.product.color like :color and o.product.size = :size " +
                                                "                 and o.product.productName like :name)",
                                        Product.class)
                                .setParameter("color", productTWP.getColor())
                                .setParameter("size", productTWP.getSize())
                                .setParameter("name", productTWP.getProductName())
                                .setMaxResults(1)
                                .getSingleResult();
                    }catch(Exception e){
                        product = listProduct.get(0);
                    }
                    orderItem.setOrder(orderEnty);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    entityManager.persist(orderItem);
                    orderItems.add(orderItem);
                    // Cap nhap lai Cart va Product

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
                Double loyatiDouble = (Double) entityManager.createQuery("select sum(o.payment.amount)/1000 from Order o where " +
                                "o.customer = :customer")
                        .setParameter("customer", customer)
                        .getSingleResult();
                long loyati = Math.round(loyatiDouble);
                customer.setLoyalty((int) loyati);
                String finalAddress = address;
                List<String> resultList = entityManager.createQuery("select a.email from Account a where a.user.userID = :userID " +
                                "and a.authProvider <> :auth")
                        .setParameter("userID", order.getCustomer().getUserID())
                        .setParameter("auth", AuthProvider.FACEBOOK)
                        .getResultList();

                String gmail = resultList.isEmpty() ? null : resultList.get(0);
                if (gmail.length() > 0) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            // Gọi phương thức gửi email
                            gmail_Service.sendGmailBill(order, amount, discount, feeShip, finalAddress, gmail, orderId);
                        } catch (Exception e) {
                            // Xử lý lỗi nếu cần
                            e.printStackTrace();
                        }
                    });
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }

        return true;
    }

    public boolean CanCreateOrder(List<CartItemDTO> cartItem) {
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
                    CustomerDTO customerDTO = null;
                    if (order.getCustomer() != null) {
                        customerDTO = new CustomerDTO(
                                order.getCustomer().getUserID(),
                                order.getCustomer().getFullName(),
                                order.getCustomer().getPhone(),
                                order.getCustomer().isActive(),
                                order.getCustomer().getDateOfBirth(),
                                order.getCustomer().getLoyalty()
                        );
                    }

                    // Chuyển đổi Payment sang PaymentDTO
                    PaymentDTO paymentDTO = null;
                    if (order.getPayment() != null) {
                        paymentDTO = new PaymentDTO(
                                order.getPayment().getPaymentId(),
                                null, // Quan hệ vòng lặp OrderDTO, có thể set null
                                order.getPayment().getPaymentMethod(),
                                order.getPayment().getAmount(),
                                order.getPayment().getPaymentDate(),
                                order.getPayment().isStatus(),
                                order.getPayment().getMomoBillId()
                        );
                    }

                    // Tạo OrderDTO
                    return new OrderDTO(
                            String.valueOf(order.getOrderId()),  // Convert orderId to String
                            customerDTO,
                            null, // Quan hệ vòng lặp OrderDTO, có thể set null
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

    @Override
    public OrderDTO getOrderById(int orderId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn Order theo orderId
            Order order = entityManager.find(Order.class, orderId);

            // Kiểm tra nếu order tồn tại
            if (order != null) {
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
            } else {
                return null; // Không tìm thấy đơn hàng
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu có lỗi trong quá trình xử lý
        } finally {
            entityManager.close();
        }
    }

    public long quantityOrderCompleted() {

        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL câu truy vấn để đếm số lượng đơn hàng có trạng thái 'COMPLETED'
            String jpql = "SELECT COUNT(o) FROM Order o WHERE o.orderStatus = 'COMPLETED'";

            // Tạo truy vấn
            Query query = entityManager.createQuery(jpql);

            // Lấy kết quả
            Object result = query.getSingleResult();

            // Trả về kết quả hoặc 0 nếu kết quả là null
            return result != null ? ((Number) result).longValue() : 0L;
        } catch (Exception e) {
            // Xử lý lỗi (nếu có)
            e.printStackTrace();
            return 0L; // Trả về 0L nếu có lỗi
        } finally {
            // Đảm bảo EntityManager được đóng để tránh rò rỉ tài nguyên
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}