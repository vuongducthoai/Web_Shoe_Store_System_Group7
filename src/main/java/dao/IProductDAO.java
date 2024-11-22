package dao;

import dto.ProductDTO;
import entity.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> findAllWithPagination(int offset, int limit);

    int countProductName(String name);
    List<ProductDTO> findByName(String name);
    List<Product> findRandomProducts(int offset, int limit, String CurrentProductName);
}
