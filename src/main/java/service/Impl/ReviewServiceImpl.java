package service.Impl;

import dao.IReviewDAO;
import dao.Impl.ReviewDAOImpl;
import dto.CustomerDTO;
import dto.ProductDTO;
import dto.ReviewDTO;
import entity.Review;
import service.IReviewService;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceImpl implements IReviewService {
    private IReviewDAO reviewDAO = new ReviewDAOImpl();
    public List<ReviewDTO> getReviewsByProductID(List<Integer> productID) {
        return reviewDAO.getReviewsByProductID(productID);
    }

    @Override
    public List<ReviewDTO> getTop5Reviews() {
        List<ReviewDTO> reviewDTOList = new ArrayList<ReviewDTO>();
        List<Review> reviewList = reviewDAO.getTop5Reviews();
        for (Review review : reviewList) {
            ReviewDTO reviewDTO = new ReviewDTO();

            //Set Review DTO
            reviewDTO.setComment(review.getComment());
            reviewDTO.setRatingValue(review.getRatingValue());
            reviewDTO.setDate(review.getDate());

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setFullName(review.getCustomer().getFullName());
            reviewDTO.setCustomer(customerDTO);
            System.out.println("123");
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(review.getProduct().getProductName());
            productDTO.setSize(review.getProduct().getSize());
            System.out.println(review.getProduct().getSize());
            reviewDTO.setProductDTO(productDTO);
            reviewDTOList.add(reviewDTO);
        }
        return reviewDTOList;
    }
}
