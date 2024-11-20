package service;

import dto.CategoryDTO;
import dto.ProductDTO;

import java.util.List;
public interface ICategoryService {
    void insert(CategoryDTO categoryDTO);
    List<CategoryDTO> listCategory();

    List<ProductDTO> productListByCategoryId(int categoryId);
}
