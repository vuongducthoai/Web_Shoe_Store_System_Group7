package dto;

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
    private List<ProductDTO> applicableProducts ;
    private double discountValue;
    private String discountType; // "percentage", "free-shipping", ...
    private int minimumLoyalty;
    private boolean isActive;
    private String promotionType;
}
