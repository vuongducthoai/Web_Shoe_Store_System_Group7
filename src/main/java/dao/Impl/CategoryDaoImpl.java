package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import dto.CategoryDTO;
import dto.ProductDTO;
import entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static util.ConvertImageStringToByteArray.convertBase64ToByteArray;

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
    public boolean insert(Category category) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.merge(category);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean remove(int categoryId) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            // Tìm đối tượng Category bằng ID
            Category category = em.find(Category.class, categoryId);
            if (category != null) {
                em.remove(category);
            } else {
                return false;
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return false;
    }


    @Override
    public List<ProductDTO> findAllProductByCategoryWithPagination(int categoryId, int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        List<ProductDTO> productDTOList = new ArrayList<>();
        try {
            //Native SQL Query
            String sql = "SELECT p.productName, MAX(p.price), SUBSTRING_INDEX(GROUP_CONCAT(TO_BASE64(p.image)), ',', 1), " +
                    "MIN(p.description), COUNT(p.productName) " +
                    "FROM Product p " +
                    "WHERE p.status = 1 " +
                    " AND p.categoryID = " + categoryId +
                    " GROUP BY p.productName";

            Query query = entityManager.createNativeQuery(sql);


            // Phân trang
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            // Thực thi truy vấn
            List<Object[]> results = query.getResultList();

            for(Object[] result : results) {
                ProductDTO productDTO = new ProductDTO();
                String productName = (String) result[0];
                double price = (double) result[1];
                String imageString = (String) result[2];
                String description = (String) result[3];
                int quantity = ((Long) result[4]).intValue();

                byte[] image = convertBase64ToByteArray(imageString);

                productDTO.setProductName(productName);
                productDTO.setPrice(price);
                productDTO.setDescription(description);
                productDTO.setImage(image);
                productDTO.setQuantity(quantity);
                productDTOList.add(productDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return productDTOList;
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
