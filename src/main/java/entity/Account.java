package entity;

import enums.AuthProvider;
import enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import java.util.List;

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

    @OneToOne(mappedBy = "account", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<TokenForgetPassword> tokenForgetPasswordList;
}
