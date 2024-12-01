package service.Impl;
import dao.IReviewDAO;
import dao.Impl.ProductDAOImpl;
import dao.Impl.ReviewDAOImpl;
import dto.CategoryDTO;
import dto.ProductDTO;
import dto.ReviewDTO;
import entity.Category;
import entity.Product;
import service.IProductService;
import service.IReviewService;

import java.util.*;
import java.util.stream.Collectors;

public class ProductServiceImpl implements IProductService {

    private ProductDAOImpl productDAO = new ProductDAOImpl();
    private IReviewDAO reviewDAO = new ReviewDAOImpl();
    private IReviewService reviewService = new ReviewServiceImpl();

    @Override
    public List<ProductDTO> findAllWithPagination(int offset, int limit) {
        List<ProductDTO> productDTOList = productDAO.findAllWithPagination(offset, limit);
        return productDTOList;
    }

    public List<ProductDTO> findByName(String name) {
        return productDAO.findByName(name);
    }
    public List<ProductDTO> findByNameProduct(String name) {
        return productDAO.findByNameProduct(name);
    }
    public Map<ProductDTO, Double> findRandomProducts(String currentProductName, int CID) {
        Map<ProductDTO, Double> result = new HashMap<>();
        List<Product> products = productDAO.findRandomProducts(currentProductName, CID);
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductID());
            productDTO.setProductName(product.getProductName());
            productDTO.setPrice(product.getPrice());
            productDTO.setImage(product.getImage());

            List<ProductDTO> productDetails = productDAO.findByName(product.getProductName());
            List<Integer> IDs = productDetails.stream()
                    .map(ProductDTO::getProductId)
                    .distinct().toList();
            List<ReviewDTO> reviews = reviewDAO.getReviewsByProductID(IDs);
            double rating = reviewService.averageRating(reviews);
            result.put(productDTO, rating);
        }

        return result;

    }
    public List<String> getDistinctProductNames() {
        // Lấy danh sách tất cả sản phẩm từ repository
        List<ProductDTO> products = productDAO.getListProductDTO();

        // Dùng Set để loại bỏ các tên sản phẩm trùng lặp
        Set<String> distinctProductNames = products.stream()
                .map(ProductDTO::getProductName) // Lấy tên sản phẩm
                .collect(Collectors.toSet()); // Dùng Set để loại bỏ trùng lặp


        return new ArrayList<>(distinctProductNames);
    }

    public List<String> getListNameProductForPromotion(int promotionId) {
        return productDAO.getProductNamesForPromotion(promotionId);
    }
}
