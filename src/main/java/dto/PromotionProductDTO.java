package dto;

import entity.Product;
import entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionProductDTO {
    private int id;
    private PromotionDTO promotion;
    private ProductDTO product;
    public PromotionProductDTO(PromotionDTO promotion, ProductDTO product)
    {

        this.promotion = promotion;
        this.product = product;
    }

}
