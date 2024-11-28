package dto;

import entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private int chatId;
    private User user;
    private String createdDate;

    public ChatDTO(int chatId, String createdDate) {
        this.chatId = chatId;
        this.createdDate = createdDate;
    }
}
