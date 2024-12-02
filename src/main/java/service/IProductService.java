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
    List<Tuple3<ProductDTO, Double, PromotionProductDTO>>  findRandomProducts(String CurrentProductName, int CID);
    List<String> getDistinctProductNames();
    List<String> getListNameProductForPromotion(int promotionId);
    List<ProductDTO> findByNameProduct(String name);
    boolean AddProduct(Product product);
    boolean UpdateProduct(Product product);
    List<ProductDTO> getListProductDTO();
    ProductDTO getProductByID(int id);
    List<Integer> getSizesByColor(String color, String productName);
    List<ProductDTO> findListProductByCategoryID(int id);
    List<ProductDTO> getListProductByName(String name);
    List<String> getProductNamesForPromotion(int promotionId);
    long getInventoryQuantity();
    Map<Integer, Integer> getQuantitiesByColor(String color, String productName);
    void updateProductByCommonInfo(String productName, double productPrice, String categoryName, String productDescription);
    List<String> getColorsByProduct(String productName);
    void deleteProductByColor(String productName, String color) throws Exception;
    boolean updateImage(String color, String productName, byte[] imageBytes);
    void deleteSize(String productName, String color, int size);
    void reduceProductInstances(String productName, String color, int size, int quantityDifference) ;
    ProductDTO getCommonInfoByName(String productName, String color) ;
    void deleteProductByName(String productName);
    void deleteProductFromCategory(String productName);

}
