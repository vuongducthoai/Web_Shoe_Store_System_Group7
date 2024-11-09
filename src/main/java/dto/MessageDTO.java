package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private int messageId;
    private ChatDTO chatDTO;
    private String content;
    private Date timeStamp;
    private Boolean isRead;
}
