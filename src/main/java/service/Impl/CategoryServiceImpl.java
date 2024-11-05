package service.Impl;

import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import entity.CategoryEntity;
import service.ICategoryService;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao userDao = new CategoryDaoImpl();
    @Override
    public List<CategoryEntity> findAll() {
        return userDao.findAll();
    }
}
