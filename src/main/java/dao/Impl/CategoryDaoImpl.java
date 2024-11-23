package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import dto.CategoryDTO;
import entity.Category;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryDaoImpl implements ICategoryDao {

    @Override
    public List<Category> categoryList() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            return entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        }
        catch (Exception e) {
            e.getMessage();
        }
        finally {
            entityManager.close();
        }
        return null;
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

    @Override
    public List<Product> findAllProductByCategoryWithPagination(int id, int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<Product> productList = null;

        try {
            // JPQL truy vấn các sản phẩm theo categoryID
            String jpql = "SELECT p FROM Product p WHERE p.category.categoryID = :id";

            TypedQuery<Product> typedQuery  = entityManager.createQuery(jpql, Product.class);

            typedQuery.setParameter("id", id);

            // Phân trang
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(limit);

            // Thực thi truy vấn
            List<Product> products = typedQuery.getResultList();

            // Loại bỏ các sản phẩm trùng tên (giữ lại 1 sản phẩm cho mỗi tên)
            Map<String, Product> uniqueProductsMap = new LinkedHashMap<>();
            for (Product product : products) {
                uniqueProductsMap.putIfAbsent(product.getProductName(), product);
            }

            // Trả về danh sách các sản phẩm không trùng tên
            return new ArrayList<>(uniqueProductsMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return productList;

    }

    public List<CategoryDTO> categoryDTOList() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String hql = "SELECT c FROM Category c";
            TypedQuery<Category> query = entityManager.createQuery(hql, Category.class);
            List<Category> categoryList = query.getResultList();
            List<CategoryDTO> categoryDTOList = new ArrayList<>();
            for (Category category : categoryList)
            {
                CategoryDTO categoryDTO = new CategoryDTO(
                        category.getCategoryID(),
                        category.getCategoryName()


                );
                categoryDTOList.add(categoryDTO);
            }
            return categoryDTOList;
        }
        catch (Exception e) {
            e.getMessage();
            return null;
        }
        finally {
            entityManager.close();
        }


    }


}
