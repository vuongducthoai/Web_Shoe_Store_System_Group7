package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenForgetPasswordDTO {
    private Long id;

    private String token;
    private LocalDateTime expireTime;
    private boolean isUsed;

    private AccountDTO accountDTO;

    public TokenForgetPasswordDTO(String token, LocalDateTime expireTime, boolean isUsed, AccountDTO accountDTO) {
        this.token = token;
        this.expireTime = expireTime;
        this.isUsed = isUsed;
        this.accountDTO = accountDTO;
    }
}
