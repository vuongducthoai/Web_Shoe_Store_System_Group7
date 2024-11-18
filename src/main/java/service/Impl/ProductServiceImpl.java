package service.Impl;

import dao.Impl.ProductDAOImpl;
import dto.ProductDTO;
import service.IProductService;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    private ProductDAOImpl productDAO = new ProductDAOImpl();

    @Override
    public List<ProductDTO> findAll() {
        return productDAO.getAllProduct();
    }
}
