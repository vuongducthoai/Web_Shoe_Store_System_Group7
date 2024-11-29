package dao;

import dto.PromotionProductDTO;

import java.time.LocalDate;
import java.util.List;

public interface IProductPromotion {
    List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate);
    PromotionProductDTO promotioOnProductInfo (String productName);
}
