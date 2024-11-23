package service.Impl;

import dao.IReviewDAO;
import dao.Impl.ReviewDAOImpl;
import dto.ReviewDTO;
import service.IReviewService;

import java.util.List;

public class ReviewServiceImpl implements IReviewService {
    private IReviewDAO reviewDAO = new ReviewDAOImpl();
    public List<ReviewDTO> getReviewsByProductID(List<Integer> productID) {
        return reviewDAO.getReviewsByProductID(productID);
    }
}
