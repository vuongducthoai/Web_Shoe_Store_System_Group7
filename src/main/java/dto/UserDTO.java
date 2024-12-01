package dto;
import enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int userID;
    private String fullName;
    private String phone;
    private boolean active;
    private AccountDTO account;
    private List<ResponseDTO> responseDTOList;

    public UserDTO(int userID, String fullName, String phone) {
        this.userID = userID;
        this.fullName = fullName;
        this.phone = phone;
    }
    public UserDTO(int userID , int accountId, RoleType role)
    {
        this.account.setAccountID(accountId);
        this.account.setRole(role);
        this.userID = userID;
        this.account = account;
    }

}


