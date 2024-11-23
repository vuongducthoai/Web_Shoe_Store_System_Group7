package service.Impl;

import dao.IOrderDao;
import dao.Impl.OrderImpl;
import dto.*;
import entity.OrderItem;
import enums.OrderStatus;
import jakarta.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import service.IOrderService;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class OrderServiceImpl implements IOrderService {
    IOrderDao orderDao = new OrderImpl();
    @Override
    public boolean CreateOrder(String json) {
        JSONObject jsonKQ = new JSONObject(json);
        String orderId = jsonKQ.getString("orderId");
        String extraData = jsonKQ.getString("extraData");

        byte[] decodedBytes = Base64.getDecoder().decode(extraData);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(decodedString);
        int idUser = jsonObject.getInt("idUser");
        JSONArray ListJsonCartItem = jsonObject.getJSONArray("ListItem");
        CustomerDTO customer = new CustomerDTO();
            customer.setUserID(idUser);
        List<OrderItemDTO> OrderItemDTOList = new ArrayList<>();
        for (int i = 0; i < ListJsonCartItem.length(); i++) {
            JSONObject item = ListJsonCartItem.getJSONObject(i);
            ProductDTO product = new ProductDTO();
                product.setProductId(item.getInt("idProduct"));
            OrderItemDTO orderItem = new OrderItemDTO();
                orderItem.setProductDTO(product);
                orderItem.setQuantity(item.getInt("quantity"));
            OrderItemDTOList.add(orderItem);
        }
        PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setAmount(jsonKQ.getInt("amount"));
            paymentDTO.setMomoBillId(orderId);
        OrderDTO order = new OrderDTO();
            order.setCustomer(customer);
            order.setOrderItems(OrderItemDTOList);
            order.setPayment(paymentDTO);
        return orderDao.CreateOrder(order);
    }

    @Override
    public boolean CanCreateOrder(List<CartItemDTO> cartItem) {
        return orderDao.CanCreateOrder(cartItem);
    }

    @Override
    public List<OrderDTO> findAllOrders() {
        return orderDao.findAllOrders();
    }

    @Override
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        return orderDao.updateOrderStatus(orderId, newStatus);
    }

    // Phương thức lấy danh sách đơn hàng đã lọc
    @Override
    public List<OrderDTO> getFilteredOrders(String searchKeyword, String orderStatus, String startDate, String endDate) {
        // Lấy toàn bộ danh sách đơn hàng
        List<OrderDTO> orderDTOList = orderDao.findAllOrders();

        // Lọc theo từ khóa
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            orderDTOList = filterOrdersByKeyword(orderDTOList, searchKeyword);
        }

        // Lọc theo trạng thái
        if (orderStatus != null && !orderStatus.isEmpty()) {
            orderDTOList = filterOrdersByStatus(orderDTOList, orderStatus);
        }

        // Lọc theo ngày
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            orderDTOList = filterOrdersByDate(orderDTOList, startDate, endDate);
        }

        return orderDTOList;
    }

    // Lọc các đơn hàng theo trạng thái
    private List<OrderDTO> filterOrdersByStatus(List<OrderDTO> orders, String status) {
        List<OrderDTO> filteredOrders = new ArrayList<>();
        for (OrderDTO order : orders) {
            if (order.getOrderStatus().name().equalsIgnoreCase(status)) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    // Lọc theo ngày
    private List<OrderDTO> filterOrdersByDate(List<OrderDTO> orderList, String startDate, String endDate) {
        List<OrderDTO> filteredList = new ArrayList<>();
        try {
            // Chuyển đổi startDate và endDate từ String sang Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            for (OrderDTO order : orderList) {
                Date orderDate = order.getOrderDate(); // Là kiểu Date, không cần parse
                if (!orderDate.before(start) && !orderDate.after(end)) { // Kiểm tra ngày nằm trong khoảng
                    filteredList.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredList;
    }

    // Lọc theo từ khóa (orderId hoặc tên khách hàng)
    private List<OrderDTO> filterOrdersByKeyword(List<OrderDTO> fullOrderList, String keyword) {
        List<OrderDTO> filteredList = new ArrayList<>();
        for (OrderDTO order : fullOrderList) {
            // Kiểm tra xem orderId hoặc tên khách hàng có chứa từ khóa không
            if (String.valueOf(order.getOrderId()).contains(keyword) ||
                    order.getCustomer().getFullName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(order);
            }
        }
        return filteredList;
    }
    @Override
    public List<OrderDTO> getOrdersByCustomerId(int customerId) {
        return orderDao.findOrdersByCustomerId(customerId);
    }

}
