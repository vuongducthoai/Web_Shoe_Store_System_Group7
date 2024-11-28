import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dao.Impl.ProductPromotionImpl;
import dto.PromotionProductDTO;

import java.time.LocalDate;
import java.util.*;

public class MAIN {
    public static void main(String[] args) {
//        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
//            em.close();
        IProductPromotion productPromotion = new ProductPromotionImpl();
        LocalDate startDate = LocalDate.parse("2023-05-30");
        LocalDate endDate = LocalDate.parse("2023-06-29");
        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop5ProductPromotionNow(startDate, endDate);
//        System.out.println(promotionProductDTOList.size());
        //        for (PromotionProductDTO promotionProductDTO : promotionProductDTOList) {
//            System.out.println(promotionProductDTO.getPromotion().getPromotionName());
//        }
    }
}
