package service;

import dto.ReviewDTO;

import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);
}
