package service;

import dto.ProductDTO;
import dto.ReviewDTO;

import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);

    List<ReviewDTO> getTop5Reviews();

    double averageRating (List<ReviewDTO> reviews);

}
