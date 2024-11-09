package dao;

import entity.Cart;

import java.util.List;


public interface ICategoryDao {
    List<Cart> findAll();
}
