package service.Impl;

import dto.CartItemDTO;
import dto.CategoryDTO;
import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import dto.ProductDTO;
import entity.Category;
import service.ICategoryService;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao categoryDao = new CategoryDaoImpl();

    public List<CategoryDTO> findAllCategories() { return categoryDao.findAllCategories(); }
}
