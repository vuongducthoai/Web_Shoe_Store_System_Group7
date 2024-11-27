package entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private int categoryID;
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}
