package service;

import dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAll();
}
