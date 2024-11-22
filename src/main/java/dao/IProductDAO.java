package dao;

import dto.AddressDTO;

import dto.ProductDTO;
import entity.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> findAllWithPagination(int offset, int limit);

    int countProductName(String name);
    boolean AddProduct(Product product);
    boolean UpdateProduct(Product product);
    List<ProductDTO> getListProductDTO();
    List<ProductDTO> findByName(String name);
    List<Product> findRandomProducts(int offset, int limit, String CurrentProductName);
}
