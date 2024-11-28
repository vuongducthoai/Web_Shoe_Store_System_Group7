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
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notifyID;

    private Date timeStamp;
    private String content;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
