package dao;

import dto.ProductDTO;
import java.util.List;

public interface IProductDAO {
    List<ProductDTO> getAllProduct();
}
