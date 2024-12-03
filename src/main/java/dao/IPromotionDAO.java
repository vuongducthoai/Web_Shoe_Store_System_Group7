package dao;

import dto.PromotionDTO;
import entity.Promotion;

import java.util.List;

public interface IPromotionDAO {
    List<PromotionDTO> getAllPromotions();
    boolean addPromotion(Promotion promotion);
    boolean updatePromotionStatus(Promotion promotion);
}
