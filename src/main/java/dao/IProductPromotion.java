package dao;

import dto.PromotionProductDTO;
import entity.PromotionProduct;

import java.time.LocalDate;
import java.util.List;

public interface IProductPromotion {
    List<PromotionProductDTO> findTop8ProductPromotionNow(LocalDate startDate, LocalDate endDate, int offset, int limit);
    PromotionProductDTO promotioOnProductInfo (String productName, int loyaty);
    int countPromotion(LocalDate startDate, LocalDate endDate);
    List<PromotionProductDTO> getAll ();
    boolean addPromotionProduct(PromotionProduct promotionProduct);
}
