package dto;

import entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private int responseID;
    private Date timeStamp;
    private String content;
    private AdminDTO admin;
    private ReviewDTO review;
}
