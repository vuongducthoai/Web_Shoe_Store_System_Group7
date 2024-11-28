package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO extends UserDTO {

    private Date dateOfBirth;
    private int loyalty;
    private AddressDTO addressDTO;
    private CartDTO cartDTO;
    private List<OrderDTO> ordersDTOList;
    private ChatDTO chatDTO;
    private List<ReviewDTO> reviewDTOList;
    private List<NotifyDTO> notifyDTOList;


    public CustomerDTO(int userID, String fullName, String phone, ChatDTO chatDTO) {
        super(userID, fullName, phone);
        this.chatDTO = chatDTO;
    }

    public CustomerDTO(int userID, String fullName, String phone, boolean active, Date dateOfBirth, int loyalty) {
        super(userID, fullName, phone,active, null, null); // Gọi constructor lớp cha (UserDTO)
        this.dateOfBirth = dateOfBirth;
        this.loyalty = loyalty;
        this.addressDTO = null;
        this.cartDTO = null;
        this.ordersDTOList = null;
        this.chatDTO = null;
        this.reviewDTOList = null;
        this.notifyDTOList = null;
    }
}
