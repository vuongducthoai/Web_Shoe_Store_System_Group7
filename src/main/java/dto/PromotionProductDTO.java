package dto;

import entity.Product;
import entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionProductDTO {
    private int id;
    private PromotionDTO promotion;
    private ProductDTO product;
}
