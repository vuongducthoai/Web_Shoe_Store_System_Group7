package service.Impl;

import dao.IOrderDao;
import dao.Impl.OrderImpl;
import dto.*;
import org.json.JSONArray;
import org.json.JSONObject;
import service.IOrderService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
}
