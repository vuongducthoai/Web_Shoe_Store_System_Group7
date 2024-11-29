package entity;


import dto.PromotionProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "promotionID", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

    public PromotionProductDTO toDTO() {
        return new PromotionProductDTO(id, promotion.toDTO(), null);
    }
}
