package service;

import dto.PromotionProductDTO;

import java.time.LocalDate;
import java.util.*;

public interface IProductPromotion {
    List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate);
}
