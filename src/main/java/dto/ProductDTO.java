package dto;

import entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    public ProductDTO(int productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public ProductDTO(int productId, String productName, double price, byte[] imageUrl, String color, int size, boolean status, String description, List<CartItemDTO> cartItemDTOList, List<OrderItemDTO> orderItemDTOList, CategoryDTO categoryDTO, PromotionDTO promotionDTO) {
        this.productId = productId;
        this.promotionDTO = promotionDTO;
        this.description = description;
        this.status = status;
        this.size = size;
        this.color = color;
        this.image = imageUrl;
        this.price = price;
        this.productName = productName;
        this.cartItemDTOList = cartItemDTOList;
        this.orderItemDTOList = orderItemDTOList;
        this.categoryDTO = categoryDTO;
    }
    public ProductDTO(String productName, double price, byte[] imageUrl, String color, int size, CategoryDTO categoryDTO, String description, boolean status) {
        this.description = description;
        this.status = status;
        this.size = size;
        this.color = color;
        this.image = imageUrl;
        this.price = price;
        this.productName = productName;
        this.categoryDTO = categoryDTO;
    }

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
    private PromotionDTO promotionDTO;
    private int quantity;
    public String getBase64Image() {
        if (image != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(image);
        }
        return null; // hoặc đường dẫn ảnh mặc định nếu không có dữ liệu
    }
}
