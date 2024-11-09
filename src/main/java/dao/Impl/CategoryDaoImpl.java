package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import dto.ProductDTO;
import entity.Category;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryDaoImpl implements ICategoryDao {
    @Override
    public List<ProductDTO> findAll() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            List<Product> products = entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
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

    @Override
    public void insert(Category category) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
