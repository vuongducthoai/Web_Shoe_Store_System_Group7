package dao;

import entity.CategoryEntity;

import java.util.List;


public interface ICategoryDao {
    List<CategoryEntity> findAll();
}
