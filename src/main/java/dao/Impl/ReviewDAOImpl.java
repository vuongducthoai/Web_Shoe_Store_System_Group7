package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IReviewDAO;
import dto.CustomerDTO;
import dto.ReviewDTO;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDAOImpl implements IReviewDAO {
    @Override
    public List<ReviewDTO> getReviewsByProductID(List<Integer> productIDs) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Query HQL
            String sql = "SELECT u.fullName, r.comment, r.ratingValue, r.date, p.productName " +
                    "FROM Review r " +
                    "JOIN Product p ON r.productID = p.productID " +
                    "JOIN User u ON r.customerID = u.userID " +
                    "WHERE p.productID IN (:productIDs)";

            // Execute Native Query
            List<Object[]> results = entityManager.createNativeQuery(sql)
                    .setParameter("productIDs", productIDs) // Truyền danh sách productIDs
                    .getResultList();

            // Chuyển đổi kết quả sang DTO
            List<ReviewDTO> reviewDTOs = new ArrayList<>();
            for (Object[] row : results) {
                CustomerDTO customer = new CustomerDTO();
                customer.setFullName((String) row[0]);

                ReviewDTO dto = new ReviewDTO();
                dto.setComment((String) row[1]);
                dto.setDate((Date) row[3]);
                dto.setRatingValue((Integer) row[2]);
                dto.setCustomer(customer);
                reviewDTOs.add(dto);
            }
            return reviewDTOs;
        } finally {
            entityManager.close();
        }
    }

}
