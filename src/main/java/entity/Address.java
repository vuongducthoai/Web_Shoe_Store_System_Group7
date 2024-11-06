package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private int id;

    private int houseNumber;
    private String streetName;
    private String province;
    private String district;
    private String city;

    public void setAddress(int houseNumber, String streetName, String province, String district, String city) {
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.province = province;
        this.district = district;
        this.city = city;
    }

    public String getFullAddress() {
        return houseNumber + " " + streetName + ", " + district + ", " + city + ", " + province;
    }
}

