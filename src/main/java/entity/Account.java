package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountID;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String providerID;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToOne(mappedBy = "account")
    private User user;

}
