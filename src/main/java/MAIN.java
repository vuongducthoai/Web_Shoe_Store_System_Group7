import JpaConfig.JpaConfig;
import dao.IProductPromotion;
import dao.Impl.ProductPromotionImpl;
import entity.PromotionProduct;
import java.util.List;

import java.time.LocalDate;

public class MAIN {
    public static void main(String[] args) {
//
//        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
//            em.close();
        IProductPromotion productPromotion = new ProductPromotionImpl();

        // Define the time range
//        LocalDate startDate = LocalDate.now().minusDays(7); // 7 days ago
        LocalDate startDate = LocalDate.parse("2022-01-06");
        LocalDate endDate = LocalDate.parse("2023-11-25");
        // today

        // Call the method with the dates
        List<PromotionProduct> promotions = productPromotion.findTop5ProductPromotionNow(startDate, endDate);

        // Print the results
        if (promotions != null) {
            promotions.forEach(promotion -> System.out.println(
                    "Product: " + promotion.getProduct().getProductName() +
                            "Description: "+ promotion.getProduct().getDescription() +
                            ", Promotion: " + promotion.getPromotion().getPromotionName()));
        } else {
            System.out.println("No promotions found.");
        }
    }
}
