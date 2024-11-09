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
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int responseID;

    private Date timeStamp;
    private String content;

    @ManyToOne
    @JoinColumn(name="adminID")
    private User admin;

    @OneToOne
    @JoinColumn(name ="reviewID")
    private Review review;

}
