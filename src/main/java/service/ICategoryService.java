package service;

import dto.CategoryDTO;
import dto.ProductDTO;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    void insert(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategory();

    List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit);

    List<CategoryDTO> findAllCategories();
    List<ProductDTO> filter(List<CategoryDTO> cartItemDTOList, String selectedCategory, Double filterMinPrice, Double filterMaxPrice, String selectedColor, String selectedSize, String selectedPromotion, String searchName);
    List<ProductDTO> distinctName(List<ProductDTO> products);
    List<ProductDTO> sortProducts(List<ProductDTO> filteredProducts, String sortOption);
    Map<String, Object> getProductInfo(List<CategoryDTO> cartItemDTOList);
    String jsonGetSoldQuantities(List<CategoryDTO> categories);
    String jsonSaginatedProducts(List<ProductDTO> products, int startIndex, int endIndex);
}
