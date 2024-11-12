package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private int cartItemId;
    private int quantity;
    private CartDTO cartDTO;
    private ProductDTO productDTO;
}
