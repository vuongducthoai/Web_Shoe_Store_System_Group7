package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import enums.DiscountType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductPromotionImpl implements IProductPromotion {
    @Override
    public List<PromotionProductDTO> findTop5ProductPromotionNow(LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<PromotionProductDTO> promotionProductDTOList = new ArrayList<>();

        try {
            String sql = "SELECT p.productName, pr.startDate, pr.endDate, p.description, p.image, pr.discountValue, pr.discountType, pr.promotionName, p.price " +
                    "FROM PromotionProduct pp " +
                    "INNER JOIN Promotion pr ON pp.promotionID = pr.promotionID " +
                    "INNER JOIN Product p ON pp.productID = p.productID " +
                    "WHERE pr.isActive = 1 " +
                    "AND p.status = 1 " +
                    "AND pr.promotionType = 'VOUCHER_PRODUCT' " +
                    "AND pr.startDate <= '" + startDate +
                    "' AND pr.endDate >=  '" + endDate +
                    "' ORDER BY pr.startDate DESC";

            Query query = entityManager.createNativeQuery(sql);
            query.setMaxResults(5); // Set to 5 if that's the correct limit

            List<Object[]> results = query.getResultList();
            System.out.println(results.size());
            for (Object[] row : results) {
                String productName = (String) row[0];
                Timestamp startTimestamp = (Timestamp) row[1];  // Cast to Timestamp
                Timestamp endTimestamp = (Timestamp) row[2];    // Cast to Timestamp

                Date startDate1 = new Date(startTimestamp.getTime());
                Date endDate1 = new Date(endTimestamp.getTime());
                System.out.println("Start Date: " + startDate1);
                System.out.println("End Date: " + endDate1);

                String description = (String) row[3];
                byte[] image = (byte[]) row[4];
                double discountValue = (double) row[5];
                String discountType = (String) row[6];
                String promotionName = (String) row[7];
                double price = (double) row[8];

                PromotionProductDTO promotionProductDTO = new PromotionProductDTO();
                PromotionDTO promotionDTO = new PromotionDTO();
                promotionDTO.setPromotionName(promotionName);
                promotionDTO.setDiscountValue(discountValue);

                DiscountType discountTypeEnum = DiscountType.valueOf(discountType);
                if (DiscountType.Percentage == discountTypeEnum) {
                    promotionDTO.setDiscountType(DiscountType.Percentage);
                } else {
                    promotionDTO.setDiscountType(DiscountType.VND);
                }

                promotionDTO.setEndDate(endDate1);
                promotionDTO.setStartDate(startDate1);
                promotionProductDTO.setPromotion(promotionDTO);

                // Ensure productDTO is initialized before usage
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductName(productName);
                productDTO.setImage(image);
                productDTO.setDescription(description);
                productDTO.setPrice(price);

                if (promotionDTO.getDiscountType().equals(DiscountType.VND)) {
                    productDTO.setPrice(productDTO.getPrice() - promotionDTO.getDiscountValue());
                } else {
                    productDTO.setPrice(productDTO.getPrice() * promotionDTO.getDiscountValue());
                }

                promotionProductDTO.setProduct(productDTO);
                promotionProductDTOList.add(promotionProductDTO);
            }
        } catch (Exception e) {
            // Log the exception or handle it as needed
            System.out.println( e.getMessage());

        } finally {
            entityManager.close();
        }
        return promotionProductDTOList;
    }

}
