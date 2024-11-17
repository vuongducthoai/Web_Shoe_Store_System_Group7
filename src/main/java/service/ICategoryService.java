package service;

import dto.CartItemDTO;
import dto.CategoryDTO;
import dto.ProductDTO;

import java.util.List;
public interface ICategoryService {
    List<CategoryDTO> findAllCategories();
}
