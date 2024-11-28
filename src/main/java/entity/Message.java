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
    @JoinColumn(name = "chatID", nullable = false) // Foreign key to Chat
    private Chat chat;

    @Column(nullable = false)
    private int userID; // Reference to the sender's ID (either Admin or Customer)

    @Column(nullable = false)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date date;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private boolean isRead; // New field to track if the message has been read
}
