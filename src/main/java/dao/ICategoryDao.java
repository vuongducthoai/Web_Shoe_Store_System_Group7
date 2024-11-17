package dao;

import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;

import java.util.List;


public interface ICategoryDao {

    List<CategoryDTO> findAllCategories();
}