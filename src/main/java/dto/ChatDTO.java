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
    private CustomerDTO customer;
    private Date createdDate;
    private List<MessageDTO> messages;

    public ChatDTO(int chatId, Date createdDate) {
        this.chatId = chatId;
        this.createdDate = createdDate;
    }
}
