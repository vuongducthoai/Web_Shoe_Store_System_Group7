import JpaConfig.JpaConfig;
import dao.Impl.UserDAOImpl;
import entity.User;


public class MAIN {
    public static void main(String[] args) {
//        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
//            em.close();
//        IProductPromotion productPromotion = new ProductPromotionImpl();
//        LocalDate startDate = LocalDate.parse("2023-05-30");
//        LocalDate endDate = LocalDate.parse("2023-06-29");
//        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop5ProductPromotionNow(startDate, endDate);
//        System.out.println(promotionProductDTOList.size());
        //        for (PromotionProductDTO promotionProductDTO : promotionProductDTOList) {
//            System.out.println(promotionProductDTO.getPromotion().getPromotionName());
//        }

        UserDAOImpl userDAO = new UserDAOImpl();
        User user1 = userDAO.getUserByAccountId(107);
        System.out.println(user1.getUserID());
    }
}
