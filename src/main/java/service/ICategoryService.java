package service;

import entity.CategoryEntity;
import java.util.List;
public interface ICategoryService {
    List<CategoryEntity> findAll();

    void insert();

}
