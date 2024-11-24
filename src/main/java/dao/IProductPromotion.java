package dao;

import entity.PromotionProduct;

import java.time.LocalDate;
import java.util.List;

public interface IProductPromotion {
    List<PromotionProduct> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate);
}
