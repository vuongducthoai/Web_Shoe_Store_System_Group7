package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import entity.Category;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
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

            // Thực thi truy vấn
            productList = entityManager.createQuery(jpql, Product.class)
                    .setParameter("id", id)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return productList;

    }
}
