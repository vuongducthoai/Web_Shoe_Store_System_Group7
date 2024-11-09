package dto;

import entity.AuthProvider;
import entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private int accountId;
    private AuthProvider authProvider;
    private String email;
    private String password;
    private String providerId;
    private RoleType role;
}
