package service;

import dto.PromotionDTO;

import java.util.List;

public interface IPromotionService {
    List<PromotionDTO> findAll();
    boolean addPromotion(PromotionDTO promotion);
    boolean updatePromotion(PromotionDTO promotion);


}
