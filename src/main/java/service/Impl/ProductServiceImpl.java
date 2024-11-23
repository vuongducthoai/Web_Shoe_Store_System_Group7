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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements IProductService {
    private ProductDAOImpl productDAO = new ProductDAOImpl();
    private IReviewDAO reviewDAO = new ReviewDAOImpl();
    private IReviewService reviewService = new ReviewServiceImpl();

    @Override
    public List<ProductDTO> findAllWithPagination(int offset, int limit) {
        List<Product> categoryList = productDAO.findAllWithPagination(offset, limit);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : categoryList) {
            ProductDTO productDTO = new ProductDTO();
            Category category = product.getCategory();
            CategoryDTO categoryDTO = new CategoryDTO();
            category.setCategoryID(product.getProductID());
            categoryDTO.setCategoryId(category.getCategoryID());
            productDTO.setCategoryDTO(categoryDTO);
            productDTO.setProductId(product.getProductID());
            productDTO.setProductName(product.getProductName());
            productDTO.setDescription(product.getDescription());
            productDTO.setCreateDate(product.getCreateDate());
            productDTO.setPrice(product.getPrice());
            productDTO.setImage(product.getImage());
            productDTO.setQuantity(productDAO.countProductName(product.getProductName()));
            System.out.println("abc: " + productDTO.getQuantity());
            System.out.println(productDTO.getQuantity());
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    public List<ProductDTO> findByName(String name) {
        return productDAO.findByName(name);
    }


    public Map<ProductDTO, Double> findRandomProducts(int offset, int limit, String currentProductName) {
        Map<ProductDTO, Double> result = new HashMap<>();
        List<Product> products = productDAO.findRandomProducts(offset, limit, currentProductName);
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
}
