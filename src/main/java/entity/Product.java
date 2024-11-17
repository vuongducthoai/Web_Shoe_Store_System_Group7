package entity;

import dto.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "product")
    private List<CartItem> orders;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "promotionID")
    private Promotion promotion;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    // Phương thức ánh xạ từ Product sang ProductDTO
    public ProductDTO productToDTO() {
        return new ProductDTO(
            this.productID,
            this.productName,
            this.price,
            this.image,
            this.color,
            this.size,
            this.status,
            this.description,
            null,
            null,
            Optional.ofNullable(this.category).map(category -> category.categoryToDTO()).orElse(null),
            Optional.ofNullable(this.promotion).map(promotion -> promotion.promotionToDTO()).orElse(null),
            this.reviews == null ? null : this.reviews.stream()
                    .map(review -> review.reviewToDTO())
                    .collect(Collectors.toList()),
            this.image == null ? "" : Base64.getEncoder().encodeToString(this.image)
        );
    }
}
