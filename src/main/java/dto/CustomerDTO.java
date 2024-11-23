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

    private int customerId;
    private Date dateOfBirth;
    private int loyalty;
    private AddressDTO addressDTO;
    private CartDTO cartDTO;
    private List<OrderDTO> ordersDTOList;
    private ChatDTO chatDTO;
    private List<ReviewDTO> reviewDTOList;
    private List<NotifyDTO> notifyDTOList;
//    private int userID;
//    private String fullName;
//    private String phone;
//    private boolean active;
//    private AccountDTO account;
//    private List<ResponseDTO> responseDTOList;
}
