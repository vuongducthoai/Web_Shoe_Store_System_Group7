package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressID;

    private int houseNumber;
    private String streetName;
    private String province;
    private String district;
    private String city;
    @OneToOne(mappedBy = "address")
    private Customer customer;
}

