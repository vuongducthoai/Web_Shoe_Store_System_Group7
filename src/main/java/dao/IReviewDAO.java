package dao;

import dto.ReviewDTO;
import entity.Review;

import java.util.List;

public interface IReviewDAO {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);
    List<ReviewDTO> getTop5Reviews();
    public boolean addReview(Review review);
    public Review getReviewsByProductId(int productId);
    public boolean updateReview(Review review);
}
