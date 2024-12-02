import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dao.IUserDAO;
import dao.Impl.ProductDAOImpl;
import dao.Impl.UserDAOImpl;
import dto.CustomerDTO;
import dto.ProductDTO;
import dto.UserDTO;
import entity.User;
import jakarta.persistence.EntityManager;
import service.IUserService;
import service.Impl.UserServiceImpl;

import java.util.List;


public class MAIN {
    public static void main(String[] args) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
            em.close();
//        IProductPromotion productPromotion = new ProductPromotionImpl();
//        LocalDate startDate = LocalDate.parse("2024-01-20");
//        LocalDate endDate = LocalDate.parse("2025-12-30");
//        int result = productPromotion.countPromotion(startDate, endDate);
//        System.out.println(result);
//        List<PromotionProductDTO> promotionProductDTOList = productPromotion.findTop8ProductPromotionNow(startDate,endDate, 0, 6);
//        System.out.println(promotionProductDTOList.size());
//        for (PromotionProductDTO promotionProductDTO : promotionProductDTOList) {
//            System.out.println(promotionProductDTO.getPromotion().getDiscountType());
//            System.out.println(promotionProductDTO.getProduct().getSellingPrice());
//        }
//
//
//        UserDAOImpl userDAO = new UserDAOImpl();
//        User user1 = userDAO.getUserByAccountId(107);
//        System.out.println(user1.getUserID());
//
//        IProductDAO productDAO = new ProductDAOImpl();
//        List<ProductDTO> productDTOList = productDAO.fin0dAllWithPagination(0, 8);
//        for (ProductDTO productDTO : productDTOList) {
//            System.out.println(productDTO.getProductName());
//        }

//        IUserService userService = new UserServiceImpl();
//        UserDTO userDTO = userService.findUserByAccoutId(118);
//        System.out.println(userDTO.getAccount().getRole());

        IUserDAO userDAO = new UserDAOImpl();
        CustomerDTO user = userDAO.getInformationUser(69);
        System.out.println(user.getFullName());

    }
}
