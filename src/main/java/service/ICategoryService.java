package service;

import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;

import java.util.List;
public interface ICategoryService {
    void insert(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategory();

    List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit);
    List<Category> categoryList();
    List<CategoryDTO> categoryDTOList();

    boolean insert(Category category);
    boolean remove(int categoryId);
}
