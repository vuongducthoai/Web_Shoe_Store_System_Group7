package dao;

import dto.ProductDTO;
import entity.Category;

import java.util.List;


public interface ICategoryDao {
    List<ProductDTO> findAll();

    void insert(Category category);
}
