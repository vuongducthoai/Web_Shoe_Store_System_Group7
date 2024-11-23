package dto;
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
}
