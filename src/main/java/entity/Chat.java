package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatID;

    @OneToOne
    @JoinColumn(name = "userID")
    private Customer customer;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Message> messages;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", columnDefinition = "DATETIME DEFAULT NOW()")
    private Date createdDate;
}
