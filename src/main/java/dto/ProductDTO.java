package dto;

import entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<CartItemDTO> cartItemDTOList;
    private List<OrderItemDTO> orderItemDTOList;
    private CategoryDTO categoryDTO;
    private PromotionDTO promotionDTO;
    private List<ReviewDTO> reviewDTOList;
    private String imageBase64;
}