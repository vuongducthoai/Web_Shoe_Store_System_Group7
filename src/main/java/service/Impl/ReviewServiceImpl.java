package service.Impl;

import dao.IReviewDAO;
import dao.Impl.ReviewDAOImpl;

import dto.ReviewDTO;
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
}
