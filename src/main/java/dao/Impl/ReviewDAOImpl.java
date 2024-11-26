package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IReviewDAO;
import dto.CustomerDTO;
import dto.ResponseDTO;
import dto.ReviewDTO;

import entity.Customer;
import entity.Product;
import entity.Review;

import dto.UserDTO;

import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Review> getTop5Reviews() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<Review> uniqueReviews = new ArrayList<>();
        try {
            // Native SQL Query
            String sql = "SELECT DISTINCT r.reviewID, r.comment, r.ratingValue, r.date, " +
                    "c.userID, u.fullName, p.productID, p.productName, p.size " +
                    "FROM Review r " +
                    "JOIN Customer c ON r.customerID = c.userID " +
                    "JOIN User u ON c.userID = u.userID " +  // Tham chiếu đúng bảng User
                    "JOIN Product p ON r.productID = p.productID " +
                    "ORDER BY r.ratingValue DESC";

            // Execute the query
            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();

            // Use a LinkedHashMap to ensure unique customers (by userID)
            Map<Integer, Review> uniqueReviewMap = new LinkedHashMap<>();

            for (Object[] row : results) {
                // Map result row to Review, Customer, and Product
                int reviewID = (int) row[0];
                String comment = (String) row[1];
                int ratingValue = (int) row[2];
                Date date = (Date) row[3];

                int customerID = (int) row[4];
                String fullName = (String) row[5];

                int productID = (int) row[6];
                String productName = (String) row[7];
                int size = (int)row[8];

                // Create Customer object
                Customer customer = new Customer();
                customer.setUserID(customerID);
                customer.setFullName(fullName);

                // Create Product object
                Product product = new Product();
                product.setProductID(productID);
                product.setProductName(productName);
                product.setSize(size);

                // Create Review object
                Review review = new Review();
                review.setReviewID(reviewID);
                review.setComment(comment);
                review.setRatingValue(ratingValue);
                review.setDate(date);
                review.setCustomer(customer);
                review.setProduct(product);

                // Add review to the map (keyed by customer ID to ensure uniqueness)
                uniqueReviewMap.putIfAbsent(customerID, review);
            }

            // Convert to list and limit to top 5
            uniqueReviews = new ArrayList<>(uniqueReviewMap.values());
            if (uniqueReviews.size() > 5) {
                uniqueReviews = uniqueReviews.subList(0, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return uniqueReviews;
    }

}
