package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IPromotionProductDAO;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.Promotion;
import entity.PromotionProduct;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PromotionProductDAOImpl implements IPromotionProductDAO {

    @Override
    public List<PromotionProductDTO> getAll() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            List<Object[]> results = entityManager.createQuery(
                    "SELECT  p.promotionID, p.startDate, p.endDate, p.isActive," +
                            " pr.productName " +
                            "FROM PromotionProduct pp " +
                            "JOIN pp.promotion p " +
                            "JOIN pp.product pr " +
                            "WHERE p.promotionType = 'VOUCHER_PRODUCT' and p.isActive=true and pr.status=true and p.startDate >= DATE(NOW())", Object[].class).getResultList();

            // Duyệt qua kết quả và ánh xạ vào đối tượng PromotionProductDTO
            return results.stream()
                    .map(result -> {
                        int promotionID = (int) result[0];
                        Date startDate = (Date) result[1];
                        Date endDate = (Date) result[2];
                        boolean isActive = (boolean) result[3];
                        String productName = (String) result[4];
                        PromotionDTO promotion = new PromotionDTO(promotionID, startDate, endDate, isActive);
                        ProductDTO product = new ProductDTO(productName);

                        // Tạo và trả về đối tượng PromotionProductDTO
                        return new PromotionProductDTO( promotion, product);
                    })
                    .collect(Collectors.toList());

        } finally {
            entityManager.close();
        }
    }
    public boolean addPromotionProduct(PromotionProduct PromotionProduct) {

        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(PromotionProduct);
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