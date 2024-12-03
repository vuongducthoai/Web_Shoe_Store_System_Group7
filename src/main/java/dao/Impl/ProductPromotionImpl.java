package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dto.ProductDTO;
import dto.PromotionDTO;
import dto.PromotionProductDTO;
import entity.PromotionProduct;
import enums.DiscountType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProductPromotionImpl implements IProductPromotion {
    @Override
    public List<PromotionProductDTO> findTop8ProductPromotionNow(LocalDate startDate, LocalDate endDate, int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<PromotionProductDTO> promotionProductDTOList = new ArrayList<>();

        try {
            String sql = "SELECT productName, startDate, endDate, description, image, discountValue, discountType, promotionName, price " +
                    "FROM ( " +
                    "   SELECT p.productName, pr.startDate, pr.endDate, p.description, p.image, pr.discountValue, pr.discountType, pr.promotionName, p.price, " +
                    "          ROW_NUMBER() OVER (PARTITION BY p.productName ORDER BY pr.startDate DESC) AS rn " +
                    "   FROM PromotionProduct pp " +
                    "   INNER JOIN Promotion pr ON pp.promotionID = pr.promotionID " +
                    "   INNER JOIN Product p ON pp.productID = p.productID " +
                    "   WHERE pr.isActive = 1 " +
                    "   AND p.status = 1 " +
//                    "   AND pr.promotionType = 'VOUCHER_PRODUCT' " +
                    "   AND pr.startDate >= ?1 " +
                    "   AND pr.endDate <= ?2 " +
                    ") AS RankedPromotions " +
                    "WHERE rn = 1 " +
                    "ORDER BY startDate DESC";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            query.setFirstResult(offset * limit);
            query.setMaxResults(limit);

            List<Object[]> results = query.getResultList();
            System.out.println(results.size());
            for (Object[] row : results) {
                String productName = (String) row[0];
                Timestamp startTimestamp = (Timestamp) row[1];  // Cast to Timestamp
                Timestamp endTimestamp = (Timestamp) row[2];    // Cast to Timestamp

                Date startDate1 = new Date(startTimestamp.getTime());
                Date endDate1 = new Date(endTimestamp.getTime());

                String description = (String) row[3];
                byte[] image = (byte[]) row[4];
                int discountValue = (int) row[5];
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
                    productDTO.setSellingPrice(productDTO.getPrice() - promotionDTO.getDiscountValue());
                } else {
                    productDTO.setSellingPrice( productDTO.getPrice() - (productDTO.getPrice() * promotionDTO.getDiscountValue() / 100));
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


    public PromotionProductDTO promotioOnProductInfo(String productName, int loyaty){
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<PromotionProductDTO> promotionProductDTOList = new ArrayList<>();
        try{
            String sql ="select * from PromotionProduct "+
            "inner join Promotion on Promotion.promotionID = PromotionProduct.promotionID "+
            "inner join Product on Product.productID = PromotionProduct.productID "+
            "where Promotion.isActive = 1 "+
            "and Promotion.promotionType = 'VOUCHER_PRODUCT' "+
            "and Promotion.minimumLoyalty <= ? "+
            "and Promotion.startDate < NOW() "+
            "and Product.productName = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, loyaty);
            query.setParameter(2, productName);
            List<Object[]> results = query.getResultList();
            for (Object[] row : results) {

                String discountType = (String) row[4];
                int discountValue = (int) row[5];
                Date endDate = (Date) row[6];
                String promotionName = (String) row[9];

                PromotionProductDTO promotionProductDTO = new PromotionProductDTO();

                PromotionDTO promotionDTO = new PromotionDTO();
                promotionDTO.setPromotionName(promotionName);
                promotionDTO.setDiscountValue(discountValue);
                DiscountType discountTypeEnum = DiscountType.valueOf(discountType);
                promotionDTO.setDiscountType(discountTypeEnum);
                promotionDTO.setEndDate(endDate);
                promotionProductDTO.setPromotion(promotionDTO);

                promotionProductDTOList.add(promotionProductDTO);

            }
        }catch(Exception e){
            System.out.println( e.getMessage());
        }
        finally{
            entityManager.close();
        }
        if(promotionProductDTOList.size() > 0)
            return promotionProductDTOList.get(0);
        return null;
    }

    @Override
    public int countPromotion(LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String sql = "SELECT COUNT(*) " +
                    "FROM ( " +
                    "   SELECT p.productName, pr.startDate, pr.endDate, p.description, p.image, pr.discountValue, pr.discountType, pr.promotionName, p.price, " +
                    "          ROW_NUMBER() OVER (PARTITION BY p.productName ORDER BY pr.startDate DESC) AS rn " +
                    "   FROM PromotionProduct pp " +
                    "   INNER JOIN Promotion pr ON pp.promotionID = pr.promotionID " +
                    "   INNER JOIN Product p ON pp.productID = p.productID " +
                    "   WHERE pr.isActive = 1 " +
                    "   AND p.status = 1 " +
//                    "   AND pr.promotionType = 'VOUCHER_PRODUCT' " +
                    "   AND pr.startDate >= ?1 " +
                    "   AND pr.endDate <= ?2 " +
                    ") AS RankedPromotions " +
                    "WHERE rn = 1";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
            Long count = (Long) query.getSingleResult();
            return count.intValue();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        } finally {
            entityManager.close();
        }

    }
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
