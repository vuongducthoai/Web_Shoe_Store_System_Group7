package service;

import dto.PromotionProductDTO;

import java.time.LocalDate;
import java.util.*;

public interface IProductPromotion {
    List<PromotionProductDTO> findTop8ProductPromotionNow(LocalDate startDate, LocalDate endDate, int offset, int limit);
    PromotionProductDTO promotioOnProductInfo (String productName);
    int countProductPromotion(LocalDate startDate, LocalDate endDate);
}
