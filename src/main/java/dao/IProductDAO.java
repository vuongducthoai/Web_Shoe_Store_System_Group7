package dao;

import entity.Product;

import java.util.List;

public interface IProductDAO {
    List<Product> findAllWithPagination(int offset, int limit);
}
