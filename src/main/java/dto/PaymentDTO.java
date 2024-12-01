package dto;

import enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private int paymentId;
    private OrderDTO orderDTO;
    private PaymentMethod paymentMethod;
    private double amount;
    private Date paymentDate;
    private boolean status;
    private String momoBillId;
}
