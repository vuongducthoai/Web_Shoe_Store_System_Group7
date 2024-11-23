package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IReviewDAO;
import dto.CustomerDTO;
import dto.ResponseDTO;
import dto.ReviewDTO;
import dto.UserDTO;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAOImpl implements IReviewDAO {
    @Override
    public List<ReviewDTO> getReviewsByProductID(List<Integer> productIDs) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {

            String productIDsString = productIDs.stream()
                    .map(String::valueOf)
                    .reduce((id1, id2) -> id1 + "," + id2)
                    .orElse("");
            String sql = "SELECT  u.fullName, r.comment, r.ratingValue, r.date, res.adminID, adminUser.fullName, res.content, res.timeStamp " +
                    "FROM Review r " +
                    "JOIN Product p ON r.productID = p.productID " +
                    "JOIN User u ON r.customerID = u.userID " +
                    "LEFT JOIN Response res on res.reviewID = r.reviewID "+
                    "LEFT JOIN User adminUser ON res.adminID = adminUser.userID "+
                    "WHERE p.productID IN (" + productIDsString + ")";


            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();


            List<ReviewDTO> reviewDTOs = new ArrayList<>();
            for (Object[] row : results) {
                CustomerDTO customer = new CustomerDTO();
                customer.setFullName((String) row[0]);

                UserDTO admin = new UserDTO();
                if (row[4] != null) {
                    admin.setUserID((Integer) row[4]);
                }
                if (row[5] != null) {
                    admin.setFullName((String) row[5]);
                }

                ResponseDTO response = new ResponseDTO();
                if (row[6] != null) {
                    response.setContent((String) row[6]);
                }
                if (row[7] != null) {
                    response.setTimeStamp((Date) row[7]);
                }
                response.setAdmin(admin);

                ReviewDTO dto = new ReviewDTO();
                dto.setComment((String) row[1]);
                dto.setDate((Date) row[3]);
                dto.setRatingValue((Integer) row[2]);
                dto.setCustomer(customer);
                if(response.getContent() == null && response.getTimeStamp() == null){
                    response = null;
                }
                dto.setResponse(response);
                reviewDTOs.add(dto);
            }
            return reviewDTOs;
        } finally {
            entityManager.close();
        }
    }


}
