package dao;

import dto.ProductDTO;
import entity.Product;

import java.util.List;

public interface IProductDAO {
    List<ProductDTO> findAllWithPagination(int offset, int limit);
    boolean AddProduct(Product product);
    boolean UpdateProduct(Product product);
    List<ProductDTO> getListProductDTO();
    ProductDTO getProductByID(int id);
    List<ProductDTO> findByName(String name);
    List<Product> findRandomProducts(String CurrentProductName, int CID);
    List<ProductDTO> findListProductByCategoryID(int id);
    List<ProductDTO> getListProductByName(String name);
}
