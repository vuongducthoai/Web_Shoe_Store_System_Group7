package entity;

import dto.CategoryDTO;
import dto.ProductDTO;
import dto.PromotionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.List;

import java.util.Date;
import java.util.stream.Collectors;

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

    private double discountValue;
    private String discountType; // "percentage", "free-shipping", ...
    private int minimumLoyalty;
    private boolean isActive;
    @OneToMany(mappedBy = "promotion")
    private List<Product> applicableProducts ;

    public PromotionDTO promotionToDTO() {
        return new PromotionDTO(
            this.promotionID,
            this.promotionName,
            this.startDate,
            this.endDate,
            null,
            this.discountValue,
            this.discountType,
            this.minimumLoyalty,
            this.isActive
        );
    }
}
