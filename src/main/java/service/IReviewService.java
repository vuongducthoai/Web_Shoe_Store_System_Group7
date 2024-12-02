package service;

import dto.ReviewDTO;
import entity.Review;

import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);

    List<ReviewDTO> getTop5Reviews();
    public boolean addReview(ReviewDTO reviewDTO);
    double averageRating (List<ReviewDTO> reviews);
    public ReviewDTO getReviewsByProductId(int productID);
    public boolean updateReview(ReviewDTO reviewDTO);
}
