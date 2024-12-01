package service;

import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;

import java.time.LocalDate;
import java.util.*;

public interface IProductPromotion {
    List<PromotionProductDTO> findTop8ProductPromotionNow(LocalDate startDate, LocalDate endDate, int offset, int limit);
    PromotionProductDTO promotioOnProductInfo (String productName);
    int countProductPromotion(LocalDate startDate, LocalDate endDate);
    boolean CheckPromotionProduct(Date start, Date end, List<String>productName);
    List<PromotionProductDTO> getAll();
    boolean InsertPromotionProduct(PromotionDTO promotion, List<String> productName);
    List<ProductDTO> getProductByName(List<String>productName);
}
