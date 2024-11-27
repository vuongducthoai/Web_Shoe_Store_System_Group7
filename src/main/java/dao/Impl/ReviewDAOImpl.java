package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IReviewDAO;
import dto.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.*;

public class ReviewDAOImpl implements IReviewDAO {
    @Override
    public List<ReviewDTO> getReviewsByProductID(List<Integer> productIDs) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {


            String productIDsString = productIDs.stream()
                    .map(String::valueOf)
                    .reduce((id1, id2) -> id1 + "," + id2)
                    .orElse("");
            String sql = "SELECT  u.fullName, r.comment, r.ratingValue, r.date, res.adminID, adminUser.fullName, res.content, res.timeStamp, r.reviewID, res.responseID,r.image " +
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

                if (row[9] != null) {
                    response.setResponseID((Integer) row[9]);
                }else response.setResponseID(0);

                response.setAdmin(admin);

                ReviewDTO dto = new ReviewDTO();
                dto.setReviewID((Integer) row[8]);
                dto.setComment((String) row[1]);
                dto.setDate((Date) row[3]);
                dto.setRatingValue((Integer) row[2]);
                dto.setCustomer(customer);
                dto.setImage((byte[]) row[10]);
                dto.setResponse(response);
                reviewDTOs.add(dto);
            }
            return reviewDTOs;
        } finally {
            entityManager.close();
        }
    }


    @Override
    public List<ReviewDTO> getTop5Reviews() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ReviewDTO> uniqueReviews = new ArrayList<>();
        try {
            // Native SQL Query
            String sql = "WITH RankedReviews AS (\n" +
                    "    SELECT u.userID,\n" +
                    "           r.comment,\n" +
                    "           r.ratingValue,\n" +
                    "           r.date,\n" +
                    "           u.fullName,\n" +
                    "           p.productName,\n" +
                    "           p.size,\n" +
                    "           ROW_NUMBER() OVER (PARTITION BY u.userID ORDER BY r.date DESC) AS rn\n" +
                    "    FROM Review r\n" +
                    "    INNER JOIN Product p ON r.productID = p.productID\n" +
                    "    INNER JOIN User u ON r.customerID = u.userID\n" +
                    ")\n" +
                    "SELECT userID,\n" +
                    "       comment,\n" +
                    "       ratingValue,\n" +
                    "       date,\n" +
                    "       fullName,\n" +
                    "       productName,\n" +
                    "       size\n" +
                    "FROM RankedReviews\n" +
                    "WHERE rn = 1\n" +
                    "ORDER BY userID;\n";

            Query query = entityManager.createNativeQuery(sql);

            query.setMaxResults(7);

            // Execute the query and get the results
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                String comment = (String) row[1];
                int ratingValue = (int) row[2];
                Date date = (Date) row[3];
                String fullName = (String) row[4];
                String productName = (String) row[5];
                int size = (int) row[6];

                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setFullName(fullName);

                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductName(productName);
                productDTO.setSize(size);

                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setComment(comment);
                reviewDTO.setRatingValue(ratingValue);
                reviewDTO.setDate(date);
                reviewDTO.setCustomer(customerDTO);
                reviewDTO.setProductDTO(productDTO);

                uniqueReviews.add(reviewDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return uniqueReviews;
    }


}
