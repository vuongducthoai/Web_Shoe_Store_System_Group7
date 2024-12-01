package dto;

import entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import enums.DiscountType;
import enums.PromotionType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private int productId;
    private String productName;
    private double price;
    private byte[] image;
    private String color;
    private int size;
    private boolean status;
    private String description;
    private LocalDateTime createDate;
    private List<CartItemDTO> cartItemDTOList;
    private List<OrderItemDTO> orderItemDTOList;
    private CategoryDTO categoryDTO;
    private List<PromotionProductDTO> promotionProducts;
    private ReviewDTO reviewDTO;
    private String imageBase64;
    private  int quantity;

    public double calculateDiscountedPrice() {
        if (promotionProducts != null && !promotionProducts.isEmpty()) {
            Date currentDate = new Date();

            for (PromotionProductDTO promotionProduct : promotionProducts) {
                PromotionDTO promotion = promotionProduct.getPromotion();
                if (promotion != null && promotion.isActive() &&
                        promotion.getStartDate() != null &&
                        promotion.getEndDate() != null &&
                        promotion.getStartDate().before(currentDate) &&
                        promotion.getEndDate().after(currentDate)) {

                    // Tính giá dựa trên loại chiết khấu
                    if (promotion.getDiscountType() == DiscountType.Percentage) {
                        return price - (price * (promotion.getDiscountValue() / 100));
                    } else if (promotion.getDiscountType() == DiscountType.VND) {
                        return price - promotion.getDiscountValue();
                    }
                }
            }
        }
        return price; // Trả về giá gốc nếu không có khuyến mãi hợp lệ
    }
}