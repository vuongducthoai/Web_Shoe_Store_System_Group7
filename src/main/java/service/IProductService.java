package service;

import dto.ProductDTO;
import dto.PromotionProductDTO;
import entity.Product;
import io.vavr.Tuple3;
import java.util.List;
import java.util.Map;

public interface IProductService {
    List<ProductDTO> findAllWithPagination(int offset, int limit);
    List<ProductDTO> findByName(String name);
    List<Tuple3<ProductDTO, Double, PromotionProductDTO>> findRandomProducts(String CurrentProductName, int CID, int loyaty);
}
