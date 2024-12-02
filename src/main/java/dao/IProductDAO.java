package dao;

import dto.ProductDTO;
import entity.Product;

import java.util.List;
import java.util.Map;

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
    List<String> getProductNamesForPromotion(int promotionId);
    long getInventoryQuantity();
    List<ProductDTO> findByNameProduct(String name);
    Map<Integer, Integer> getQuantitiesByColor(String color, String productName);
    void updateProductByCommonInfo(String productName, double productPrice, String categoryName, String productDescription);
    List<String> getColorsByProduct(String productName);
    void deleteProductByColor(String productName, String color) throws Exception;
    boolean updateImage(String color, String productName, byte[] imageBytes);
    List<Integer> getSizesByColor(String color, String productName);
    void deleteSize(String productName, String color, int size);
    void reduceProductInstances(String productName, String color, int size, int quantityDifference) ;
    ProductDTO getCommonInfoByName(String productName, String color) ;
    void deleteProductByName(String productName);
    void deleteProductFromCategory(String productName);

}
