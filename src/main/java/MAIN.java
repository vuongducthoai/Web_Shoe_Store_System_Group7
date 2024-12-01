import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dao.Impl.ProductPromotionImpl;
import dto.PromotionProductDTO;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;


public class MAIN {
    public static void main(String[] args) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
            em.close();
        IProductPromotion productPromotion = new ProductPromotionImpl();
        LocalDate startDate = LocalDate.parse("2024-01-20");
        LocalDate endDate = LocalDate.parse("2025-12-30");
//        int result = productPromotion.countPromotion(startDate, endDate);
//        System.out.println(result);
        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop8ProductPromotionNow(startDate,endDate, 0, 6);
//        System.out.println(promotionProductDTOList.size());
        for (PromotionProductDTO promotionProductDTO : promotionProductDTOList) {
            System.out.println(promotionProductDTO.getPromotion().getDiscountType());
            System.out.println(promotionProductDTO.getProduct().getSellingPrice());
        }
//
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        User user1 = userDAO.getUserByAccountId(107);
//        System.out.println(user1.getUserID());
    }
}
