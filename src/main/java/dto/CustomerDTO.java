package dto;

import entity.Chat;
import enums.RoleType;
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
    private Boolean lastMessageStatus;


    public CustomerDTO(int userID, String fullName, String phone) {
        super(userID, fullName, phone);
    }
    public CustomerDTO(int userID, String fullName, String phone, Boolean status) {
        super(userID, fullName, phone);
        this.lastMessageStatus = status;
    }

    public CustomerDTO(int userID, String fullName, String phone, int accountId, RoleType role)
    {
        super(userID, accountId, role);
        this.getFullName();
        this.setPhone(phone);
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
