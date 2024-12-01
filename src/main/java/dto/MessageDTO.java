package dto;

import entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private int MessageID;
    private int chatId;
    private int userId;
    private String content;
    private Timestamp date;
    private boolean isRead;
}
