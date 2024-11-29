package service;

import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;

import java.util.Date;
import java.util.List;

public interface IPromotionProductService {

    boolean CheckPromotionProduct(Date start, Date end, List<String>productName);
    List<PromotionProductDTO> getAll();
    boolean InsertPromotionProduct(PromotionDTO promotion, List<String> productName);
    List<ProductDTO> getProductByName(List<String>productName);
}
