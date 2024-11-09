package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name="cardID")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productID") // khoa ngoai tham chieu den cart
    private  Product product;

    private int quantity;
}
