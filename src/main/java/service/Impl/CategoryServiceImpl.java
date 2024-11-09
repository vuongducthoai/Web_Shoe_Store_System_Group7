package service.Impl;

import dto.CategoryDTO;
import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import dto.ProductDTO;
import entity.Category;
import service.ICategoryService;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<ProductDTO> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());

        categoryDao.insert(category);
    }

}
