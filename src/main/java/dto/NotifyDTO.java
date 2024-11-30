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
public class NotifyDTO {
    private int notifyId;
    private String content;
    private Date timeStamp;
    private int customerId;


}
