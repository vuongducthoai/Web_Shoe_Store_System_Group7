package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promotionID;
    private String promotionName;
    private Date startDate;
    private Date endDate;

    @OneToMany(mappedBy = "promotion")
    private List<Product> applicableProducts ;
    private double discountValue; //10: 10%.
    private String discountType; // "percentage", fixed-amount
    private int minimumLoyalty;
    private boolean isActive;

}
