package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.PromotionProduct;
import enums.DiscountType;
import enums.PromotionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    @Override
    public List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<PromotionProductDTO> promotionProductDTOList = new ArrayList<>();
        try {
            // Native SQL query using positional parameters
            String sql = "SELECT p.productName, pr.startDate, pr.endDate, p.description, p.image, pr.discountValue, pr.discountType, pr.promotionName " +
                    "FROM PromotionProduct pp " +
                    "INNER JOIN Promotion pr ON pp.promotionID = pr.promotionID " +
                    "INNER JOIN Product p ON pp.productID = p.productID " +
                    "WHERE pr.isActive = ?1 " +
                    "AND p.status = ?2 " +
                    "AND pr.promotionType = ?3 " +
                    "AND pr.startDate <= ?4 " +
                    "AND pr.endDate >= ?5 " +
                    "ORDER BY pr.startDate DESC";

            // Create the native SQL query
            Query query = entityManager.createNativeQuery(sql);

            // Set positional parameters
            query.setParameter(1, 1);
            query.setParameter(2, 1);
            query.setParameter(3, PromotionType.VOUCHER_PRODUCT);
            query.setParameter(4, Date.valueOf(startDate));
            query.setParameter(5, Date.valueOf(endDate));

            // Limit the result to top 5 products
            query.setMaxResults(7);

            // Execute the query and get results
            List<Object[]> results = query.getResultList();
            for (Object[] row : results) {
                String productName = (String) row[0];
                LocalDate startDate1 = (LocalDate) row[1];
                LocalDate endDate1 = (LocalDate) row[2];
                String description = (String) row[3];


//                PromotionProductDTO promotionProductDTO = new PromotionProductDTO();
//
//                PromotionDTO promotionDTO = new PromotionDTO();
//                promotionDTO.setPromotionName(promotionProduct.getPromotion().getPromotionName());
//                promotionDTO.setDiscountValue(promotionProduct.getPromotion().getDiscountValue());
//                promotionDTO.setDiscountType(promotionProduct.getPromotion().getDiscountType());
//                promotionDTO.setEndDate(promotionProduct.getPromotion().getEndDate());
//                promotionDTO.setStartDate(promotionProduct.getPromotion().getStartDate());
//                promotionProductDTO.setPromotion(promotionDTO);
//
//                ProductDTO productDTO = new ProductDTO();
//                productDTO.setProductName(promotionProduct.getProduct().getProductName());
//                if(promotionProduct.getPromotion().getDiscountType().equals(DiscountType.VND)){
//                    productDTO.setPrice(promotionProduct.getProduct().getPrice() - promotionProduct.getPromotion().getDiscountValue());
//                } else {
//                    productDTO.setPrice(promotionProduct.getProduct().getPrice()*promotionProduct.getPromotion().getDiscountValue());
//                }
//                productDTO.setImage(promotionProduct.getProduct().getImage());
//                productDTO.setDescription(promotionProduct.getProduct().getDescription());
//                promotionProductDTO.setProduct(productDTO);
//                promotionProductDTOList.add(promotionProductDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return promotionProductDTOList;
    }

}
