package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewID;
    private int ratingValue;
    private Date date;
    private CustomerDTO customer;
    private String comment;
    private ResponseDTO response;
    private ProductDTO productDTO;
    private byte[] image;
    public String getBase64Image() {
        if (image != null) {
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(image);
        }
        return null; // hoặc đường dẫn ảnh mặc định nếu không có dữ liệu
    }

}
