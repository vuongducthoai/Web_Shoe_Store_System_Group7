package service;

import dto.CategoryDTO;
import dto.ProductDTO;

import java.util.List;
public interface ICategoryService {
    List<ProductDTO> findAll();

    void insert(CategoryDTO categoryDTO);
}
