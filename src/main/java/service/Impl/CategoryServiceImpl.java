package service.Impl;

import dto.CategoryDTO;
import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import dto.ProductDTO;
import entity.Category;
import entity.Product;
import service.ICategoryService;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryDao.insert(category);
    }

    @Override
    public List<CategoryDTO> listCategory() {
        List<Category> list = categoryDao.categoryList();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : list) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryId(category.getCategoryID());
            categoryDTO.setCategoryName(category.getCategoryName());
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit) {
        List<Product> categoryList = categoryDao.findAllProductByCategoryWithPagination(categoryId, offset, limit);
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : categoryList) {
            ProductDTO productDTO = new ProductDTO();
            if (productDTO.hasImage()) {
                System.out.println("Product has an image.");
            } else {
                System.out.println("Product does not have an image.");
            }
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
