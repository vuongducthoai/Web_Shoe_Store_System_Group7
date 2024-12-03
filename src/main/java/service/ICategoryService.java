package service;

import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    List<CategoryDTO> findAllCategories();
    Map<String, Object> getFilteredAndSortedProducts(
            List<CategoryDTO> categoryDTOList,
            String selectedPromotion,
            String selectedCategory,
            String selectedSize,
            String selectedColor,
            String searchName,
            String minPriceParam,
            String maxPriceParam,
            String sortOption,
            String pageParam
    );
    List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit);
    void insert(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategory();

    List<Category> categoryList();
    List<CategoryDTO> categoryDTOList();
    boolean remove(int categoryId);
}
