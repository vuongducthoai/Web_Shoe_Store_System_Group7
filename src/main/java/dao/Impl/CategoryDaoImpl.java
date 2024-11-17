package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import dto.*;
import entity.*;
import jakarta.persistence.EntityManager;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDaoImpl implements ICategoryDao {
    @Override
    public List<CategoryDTO> findAllCategories() {
        try (EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager()) {
            try {
                // Truy vấn tất cả danh mục và sản phẩm liên quan
                List<Category> categories = entityManager.createQuery(
                    "SELECT c FROM Category c LEFT JOIN FETCH c.products", Category.class)
                    .getResultList();

                // Ánh xạ từ Category sang CategoryDTO
                return categories.stream()
                    .map(category -> new CategoryDTO(
                        category.getCategoryID(),
                        category.getCategoryName(),
                        category.getProducts().stream()
                            .map(product -> product.productToDTO())  // Sử dụng phương thức riêng cho việc ánh xạ sản phẩm
                            .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
            } finally {
                entityManager.close();
            }
        }
    }
}
