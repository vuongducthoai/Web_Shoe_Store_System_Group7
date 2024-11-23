package dao;

import dto.CategoryDTO;
import entity.Category;
import entity.Product;

import java.util.List;


public interface ICategoryDao {
    List<Category> categoryList();
    List<CategoryDTO> categoryDTOList();

    void insert(Category category);

    List<Product> findAllProductByCategoryWithPagination(int id, int offset, int limit);
}
