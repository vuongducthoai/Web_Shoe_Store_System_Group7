package dto;

import enums.DiscountType;
import enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PromotionDTO {
    private int promotionId;
    private String promotionName;
    private Date startDate;
    private Date endDate;
    private List<PromotionProductDTO> promotionProducts;
    private double discountValue;
    private DiscountType discountType; // "percentage", "free-shipping", ...
    private int minimumLoyalty;
    private boolean isActive;
    private PromotionType promotionType;
}