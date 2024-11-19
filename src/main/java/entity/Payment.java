package entity;

import enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private double amount;
    private Date paymentDate;
    private boolean status;
    private String momoBillId;
}
