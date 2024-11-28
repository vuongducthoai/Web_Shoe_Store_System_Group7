package dao;

import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;
import entity.Product;

import java.util.List;


public interface ICategoryDao {
    List<Category> categoryList();
    List<CategoryDTO> categoryDTOList();

    boolean insert(Category category);
    boolean remove(int categoryId);
    List<ProductDTO> findAllProductByCategoryWithPagination(int id, int offset, int limit);
}
