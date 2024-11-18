package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import dto.ProductDTO;
import entity.Product;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDAOImpl implements IProductDAO {


    @Override
    public List<ProductDTO> getAllProduct() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            List<Product> products = entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
            System.out.println(products);
            return products.stream()
                    .map(product -> new ProductDTO(
                            product.getProductID(),
                            product.getProductName(),
                            product.getPrice()
                            // Thêm các trường cần thiết từ Product sang ProductDTO
                    ))
                    .collect(Collectors.toList());
        } finally {
            entityManager.close();
        }
    }
}
