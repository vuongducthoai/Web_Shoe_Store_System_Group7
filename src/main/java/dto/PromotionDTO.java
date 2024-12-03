package dto;

import entity.Promotion;
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
    private int discountValue;
    private DiscountType discountType; // "percentage", "free-shipping", ...
    private int minimumLoyalty;
    private boolean isActive;
    private PromotionType promotionType;

    public PromotionDTO(int promotionId, String promotionName, Date startDate, Date endDate, int discountValue, DiscountType discountType, int minimumLoyalty, PromotionType promotionType, boolean isActive) {
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.minimumLoyalty = minimumLoyalty;
        this.promotionType = promotionType;
        this.isActive = isActive;

    }
    public PromotionDTO(int promotionId ,Date startDate, Date endDate, boolean isActive )
    {
        this.promotionId = promotionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }
}
