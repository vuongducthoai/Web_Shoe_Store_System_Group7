package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import entity.PromotionProduct;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    @Override
    public List<PromotionProduct> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
     try{
         String jqpl = "SELECT pp FROM PromotionProduct pp " +
                 "WHERE pp.promotion.startDate <= :endDate " +
                 "AND pp.promotion.endDate >= :startDate " +
                 "AND pp.promotion.isActive = true " +
                 "AND pp.promotion.promotionType = :promotionType " +
                 "ORDER BY pp.promotion.discountValue DESC";
         System.out.println("123");
         TypedQuery<PromotionProduct> typedQuery = entityManager.createQuery(jqpl, PromotionProduct.class);
         typedQuery.setParameter("startDate", Date.valueOf(startDate));
         typedQuery.setParameter("endDate", Date.valueOf(endDate));
         typedQuery.setParameter("promotionType", PromotionType.VOUCHER_PRODUCT);
         List<PromotionProduct> promotionProducts = typedQuery.getResultList();

         return promotionProducts;
     } catch (Exception e) {
         e.printStackTrace();
     }
     finally{
         entityManager.close();
     }
     return null;
    }
}
