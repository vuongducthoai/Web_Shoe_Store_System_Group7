package dto;

import enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private String orderId;
    private CustomerDTO customer;
    private List<OrderItemDTO> orderItems;
    private PaymentDTO payment;
    private String shippingAddress;
    private OrderStatus orderStatus;
    private Date orderDate;
}
