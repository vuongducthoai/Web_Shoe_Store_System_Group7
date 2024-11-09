package dao;

import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Cart;
import entity.Category;
import entity.Product;

import java.util.List;


public interface ICategoryDao {
    List<ProductDTO> findAll();

    void insert(Category category);
}
