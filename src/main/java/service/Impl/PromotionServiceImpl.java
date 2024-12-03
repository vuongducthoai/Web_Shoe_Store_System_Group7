package service.Impl;

import dao.Impl.PromotionDAOImpl;
import dto.PromotionDTO;
import entity.Promotion;
import service.IPromotionService;

import java.util.List;

public class PromotionServiceImpl implements IPromotionService {
    private final PromotionDAOImpl promotionsDAO = new PromotionDAOImpl();
    @Override
    public List<PromotionDTO> findAll() {
        return  promotionsDAO.getAllPromotions();
    }
    public  boolean addPromotion(PromotionDTO promotion) {

        Promotion promotionEntity = new Promotion();
        promotionEntity.setPromotionName(promotion.getPromotionName());
        promotionEntity.setStartDate(promotion.getStartDate());
        promotionEntity.setEndDate(promotion.getEndDate());
        promotionEntity.setActive(promotion.isActive());
        promotionEntity.setDiscountType(promotion.getDiscountType());
        promotionEntity.setPromotionType(promotion.getPromotionType());
        promotionEntity.setDiscountValue(promotion.getDiscountValue());
        return promotionsDAO.addPromotion(promotionEntity);

    }
    public boolean updatePromotion(PromotionDTO promotion) {

        Promotion promotionEntity = new Promotion();
        promotionEntity.setActive(promotion.isActive());
        promotionEntity.setPromotionID(promotion.getPromotionId());
        return promotionsDAO.updatePromotionStatus(promotionEntity);
    }
}
