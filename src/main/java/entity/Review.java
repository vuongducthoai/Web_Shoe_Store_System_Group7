package entity;

import dto.ReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;
    private int ratingValue;
    private Date date;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @OneToOne(mappedBy = "review")
    private Response response;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    public ReviewDTO reviewToDTO() {
        return new ReviewDTO(
            reviewID,
            ratingValue,
            date,
            comment,
            null,
            null,
            null
        );
    }
}
