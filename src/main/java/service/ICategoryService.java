package service;

import java.util.List;
import java.util.Map;
import dto.*;

public interface ICategoryService {
    void insert(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategory();
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
}
