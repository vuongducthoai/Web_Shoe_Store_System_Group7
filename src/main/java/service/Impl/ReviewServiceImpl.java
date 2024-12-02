package service.Impl;

import dao.IReviewDAO;
import dao.Impl.ReviewDAOImpl;

import dto.CustomerDTO;
import dto.ProductDTO;
import dto.ReviewDTO;
import entity.Address;
import entity.Customer;
import entity.Product;
import entity.Review;
import service.IReviewService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ReviewServiceImpl implements IReviewService {
    private IReviewDAO reviewDAO = new ReviewDAOImpl();

    public List<ReviewDTO> getReviewsByProductID(List<Integer> productID) {
        return reviewDAO.getReviewsByProductID(productID);
    }

    @Override
    public List<ReviewDTO> getTop5Reviews() {
        return reviewDAO.getTop5Reviews();
    }

    public double averageRating(List<ReviewDTO> reviews) {
        double avg = 0;
        for (ReviewDTO review : reviews) {
            avg += review.getRatingValue();
        }

        // Nếu reviews không rỗng
        if (reviews.size() > 0) {
            avg = avg / reviews.size();
        }

        // Định dạng giá trị trung bình với 2 chữ số sau dấu thập phân
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.00",symbols);
        return Double.parseDouble(df.format(avg));  // Trả về giá trị dưới dạng số double với 2 chữ số sau dấu thập phân
    }
    @Override
    public boolean addReview(ReviewDTO reviewDTO) {
        try {

            Review review = new Review();

            Customer customer = new Customer();
            customer.setUserID(reviewDTO.getCustomer().getUserID());

            Product product = new Product();
            product.setProductID(reviewDTO.getProductDTO().getProductId());

            review.setCustomer(customer);
            review.setProduct(product);
            review.setRatingValue(reviewDTO.getRatingValue());
            review.setDate(reviewDTO.getDate());
            review.setImage(reviewDTO.getImage());
            review.setComment(reviewDTO.getComment());

            return reviewDAO.addReview(review);
            // Gọi DAO để thêm review
        } catch (Exception e) {
            System.out.println("Error in service updateAddress: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean updateReview(ReviewDTO reviewDTO) {
        try {

            Review review = new Review();

            Customer customer = new Customer();
            customer.setUserID(reviewDTO.getCustomer().getUserID());

            Product product = new Product();
            product.setProductID(reviewDTO.getProductDTO().getProductId());

            review.setReviewID(reviewDTO.getReviewID());
            review.setCustomer(customer);
            review.setProduct(product);
            review.setRatingValue(reviewDTO.getRatingValue());
            review.setDate(reviewDTO.getDate());
            review.setImage(reviewDTO.getImage());
            review.setComment(reviewDTO.getComment());

            return reviewDAO.updateReview(review);
            // Gọi DAO để cập nhật Address
        } catch (Exception e) {
            System.out.println("Error in service updateAddress: " + e.getMessage());
            return false;
        }
    }
    @Override
    public ReviewDTO getReviewsByProductId(int productID) {
        // Kiểm tra nếu productID hợp lệ (không phải 0 hoặc giá trị không hợp lệ khác)
        if (productID > 0) {
            // Lấy Review từ DAO
            Review review = reviewDAO.getReviewsByProductId(productID);

            // Nếu không tìm thấy review, trả về null hoặc một giá trị mặc định
            if (review == null) {
                return null;
            }

            // Tạo đối tượng ReviewDTO và CustomerDTO, ProductDTO
            ReviewDTO reviewDTO = new ReviewDTO();
            CustomerDTO customerDTO = new CustomerDTO();
            ProductDTO productDTO = new ProductDTO();

            // Gán thông tin từ review vào ReviewDTO, CustomerDTO và ProductDTO
            customerDTO.setUserID(review.getCustomer().getUserID());
            productDTO.setProductId(review.getProduct().getProductID());

            reviewDTO.setReviewID(review.getReviewID());
            reviewDTO.setCustomer(customerDTO);
            reviewDTO.setProductDTO(productDTO);
            reviewDTO.setImage(review.getImage());
            reviewDTO.setComment(review.getComment());
            reviewDTO.setRatingValue(review.getRatingValue());
            reviewDTO.setDate(review.getDate());

            return reviewDTO;
        }
        // Trả về null nếu productID không hợp lệ
        return null;
    }

}
