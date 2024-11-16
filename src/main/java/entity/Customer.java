package entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressID")
    private Address address;

    @OneToOne
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    private int loyalty;
    private boolean active;


    @OneToMany(mappedBy = "customer")
    private List<Review> reviewList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatID")
    private Chat chat;

    @OneToMany(mappedBy = "customer")
    private List<Notify> notifyList;
}
