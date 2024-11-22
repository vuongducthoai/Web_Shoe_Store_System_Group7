package entity;

import enums.DiscountType;
import enums.PromotionType;
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
    private List<PromotionProduct> promotionProducts;

    private double discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // "percentage", , ...

    private int minimumLoyalty;
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;
}
