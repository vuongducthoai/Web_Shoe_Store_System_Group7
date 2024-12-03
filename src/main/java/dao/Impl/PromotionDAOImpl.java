package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IPromotionDAO;
import dto.PromotionDTO;
import entity.Promotion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.stream.Collectors;

public class PromotionDAOImpl implements IPromotionDAO {
    @Override
    public List<PromotionDTO> getAllPromotions() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn danh sách Promotion
            List<Promotion> promotions = entityManager.createQuery("SELECT p FROM Promotion p WHERE p.isActive=true ", Promotion.class).getResultList();

            return promotions.stream()
                    .map(promotion -> {
                        // Tạo PromotionDTO hoàn chỉnh với danh sách PromotionProductDTO
                        return new PromotionDTO(
                                promotion.getPromotionID(),
                                promotion.getPromotionName(),
                                promotion.getStartDate(),
                                promotion.getEndDate(),
                                promotion.getDiscountValue(),
                                promotion.getDiscountType(),
                                promotion.getMinimumLoyalty(),
                                promotion.getPromotionType(),
                                promotion.isActive()

                        );
                    })
                    .collect(Collectors.toList());
        } finally {
            entityManager.close();
        }
    }


    public boolean addPromotion(Promotion promotion) {

        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(promotion); // Đây là cách chính xác khi lưu entity mới
            entityManager.flush();
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    public boolean updatePromotionStatus(Promotion promotion) {

        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(promotion); // Đây là cách chính xác khi lưu entity mới
            entityManager.flush();
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }


}
