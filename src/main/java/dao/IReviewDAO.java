package dao;

import dto.ReviewDTO;

import java.util.List;

public interface IReviewDAO {
    List<ReviewDTO> getReviewsByProductID(List<Integer> productID);
}
