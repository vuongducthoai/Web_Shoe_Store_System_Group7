package dao;

import dto.ReviewDTO;
import entity.Review;

import java.util.List;

public interface IReviewDAO {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);
    List<Review> getTop5Reviews();

}
