package service.Impl;
import dao.IReviewDAO;
import dao.Impl.ProductDAOImpl;
import dao.Impl.ReviewDAOImpl;
import dto.ProductDTO;
import dto.PromotionProductDTO;
import dto.ReviewDTO;
import entity.Product;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import service.IProductPromotion;
import service.IProductService;
import service.IReviewService;

import java.util.*;
import java.util.stream.Collectors;

public class ProductServiceImpl implements IProductService {

    private ProductDAOImpl productDAO = new ProductDAOImpl();
    private IReviewDAO reviewDAO = new ReviewDAOImpl();
    private IProductPromotion productPromotion = new ProductPromotionImpl();
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

    @Override
    public boolean AddProduct(Product product) {
        return productDAO.AddProduct(product);
    }

    @Override
    public boolean UpdateProduct(Product product) {
        return false;
    }

    @Override
    public List<ProductDTO> getListProductDTO() {
        return productDAO.getListProductDTO();
    }

    @Override
    public ProductDTO getProductByID(int id) {
        return null;
    }

    @Override
    public List<ProductDTO> findListProductByCategoryID(int id) {
        return List.of();
    }

    @Override
    public List<ProductDTO> getListProductByName(String name) {
        return productDAO.getListProductByName(name);
    }

    @Override
    public List<String> getProductNamesForPromotion(int promotionId) {
        return List.of();
    }

    @Override
    public long getInventoryQuantity() {
        return 0;
    }

    @Override
    public Map<Integer, Integer> getQuantitiesByColor(String color, String productName) {
        return productDAO.getQuantitiesByColor(color, productName);
    }

    @Override
    public void updateProductByCommonInfo(String productName, double productPrice, String categoryName, String productDescription) {
        productDAO.updateProductByCommonInfo(productName, productPrice, categoryName, productDescription);
    }

    @Override
    public List<String> getColorsByProduct(String productName) {
        return productDAO.getColorsByProduct(productName);
    }

    @Override
    public void deleteProductByColor(String productName, String color) throws Exception {
        productDAO.deleteProductByColor(productName, color);
    }

    @Override
    public boolean updateImage(String color, String productName, byte[] imageBytes) {
        return productDAO.updateImage(color, productName, imageBytes);
    }

    @Override
    public List<Integer> getSizesByColor(String color, String productName) {
        return productDAO.getSizesByColor(color, productName);
    }

    @Override
    public void deleteSize(String productName, String color, int size) {
        productDAO.deleteSize(productName, color, size);
    }

    @Override
    public void reduceProductInstances(String productName, String color, int size, int quantityDifference) {
        productDAO.reduceProductInstances(productName, color, size, quantityDifference);
    }

    @Override
    public ProductDTO getCommonInfoByName(String productName, String color) {
        return productDAO.getCommonInfoByName(productName, color);
    }

    @Override
    public void deleteProductByName(String productName) {
        productDAO.deleteProductByName(productName);
    }

    @Override
    public void deleteProductFromCategory(String productName) {

    }

    public List<Tuple3<ProductDTO, Double, PromotionProductDTO>> findRandomProducts(String currentProductName, int CID, int loyaty) {
        List<Tuple3<ProductDTO, Double, PromotionProductDTO>> resultList = new ArrayList<>();
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

            PromotionProductDTO promotionProductDTO = new PromotionProductDTO();
            promotionProductDTO = productPromotion.promotioOnProductInfo(product.getProductName(), loyaty);

            Tuple3<ProductDTO, Double, PromotionProductDTO> tuple = Tuple.of(productDTO, rating, promotionProductDTO);
            resultList.add(tuple);
        }

        return resultList;
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
