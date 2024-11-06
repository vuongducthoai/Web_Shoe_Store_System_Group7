package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    @OneToOne
    @JoinColumn(name = "address_id") // Tên cột khóa ngoại trong bảng Customer
    private Address address;

    private int loyalty;
    private boolean active;
}
