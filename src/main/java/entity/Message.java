package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    @ManyToOne
    @JoinColumn(name = "chatID", nullable = false)
    private Chat chat;

    private String content;
    private Date timeStamp;

    private boolean isRead;
}
