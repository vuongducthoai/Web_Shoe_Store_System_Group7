package service;

import entity.Cart;

import java.util.List;
public interface ICategoryService {
    List<Cart> findAll();

    void insert();

}
