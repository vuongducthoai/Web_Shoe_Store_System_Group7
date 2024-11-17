package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewID;
    private int ratingValue;
    private Date date;
    private String comment;
    private CustomerDTO customer;
    private ResponseDTO response;
    private ProductDTO product;
}
