package service.Impl;
import dao.Impl.ProductDAOImpl;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;
import entity.Product;
import service.IProductService;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements IProductService {
    private ProductDAOImpl productDAO = new ProductDAOImpl();

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
            productDTO.setPrice(product.getPrice());
            productDTO.setImage(product.getImage());
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }
}
