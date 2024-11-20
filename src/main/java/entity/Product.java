package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    private String productName;
    private double price;

    @Lob
    private byte[] image;

    private String color;
    private int size;
    private boolean status;
    private String description;

    @OneToMany(mappedBy = "product") // tham chieu den "product" trong cardItem
    private List<CartItem> orders;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotionID")
    private Promotion promotion;

}
