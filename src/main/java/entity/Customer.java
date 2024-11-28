package entity;

import java.util.Date;
import java.util.List;
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
public class Customer extends User {

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "addressID")
    private Address address;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    private int loyalty;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    private Chat chat;

    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private List<Review> reviewList;

}
