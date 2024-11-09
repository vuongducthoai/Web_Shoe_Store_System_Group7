package service.Impl;

import entity.Cart;
import dao.ICategoryDao;
import dao.Impl.CategoryDaoImpl;
import service.ICategoryService;

import java.util.List;

public class CategoryServiceImpl implements ICategoryService {
    ICategoryDao userDao = new CategoryDaoImpl();
    @Override
    public List<Cart> findAll() {
        return userDao.findAll();
    }

    @Override
    public void insert() {

    }
}
