package service;

import dto.ProductDTO;
import entity.Product;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAllWithPagination(int offset, int limit);
    List<ProductDTO> findByName(String name);
    List<ProductDTO> findRandomProducts(int offset, int limit, String CurrentProductName);
}
