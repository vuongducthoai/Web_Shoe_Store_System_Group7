package dto;
import dto.UserDTO;
import entity.Response;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AdminDTO extends UserDTO {
    private List<Response> responses;
}

