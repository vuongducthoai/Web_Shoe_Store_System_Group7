package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICategoryDao;
import dto.CategoryDTO;
import dto.ProductDTO;

import entity.Category;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
            String hql = "WITH RankedProducts AS (\n" +
                    "    SELECT \n" +
                    "        p.productName, \n" +
                    "        p.price, \n" +
                    "        p.image, \n" +
                    "        p.description, \n" +
                    "        ROW_NUMBER() OVER (PARTITION BY p.productName ORDER BY p.price DESC) AS rn, \n" +
                    "        COUNT(*) OVER (PARTITION BY p.productName) AS product_count, \n" +
                    "        p.categoryId  \n" +
                    "    FROM Product p \n" +
                    "    WHERE p.status = 0\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    rp.productName, \n" +
                    "    MAX(rp.price) AS max_price, \n" +
                    "    -- Lấy ảnh đầu tiên trong mỗi nhóm sản phẩm, không cần GROUP_CONCAT\n" +
                    "    (SELECT rp1.image \n" +
                    "     FROM RankedProducts rp1 \n" +
                    "     WHERE rp1.productName = rp.productName AND rp1.rn = 1) AS image,\n" +
                    "    MIN(rp.description) AS min_description, \n" +
                    "    rp.product_count, \n" +
                    "    c.categoryName  \n" +
                    "FROM RankedProducts rp \n" +
                    "JOIN Category c ON rp.categoryId = c.categoryId  \n" +
                    "WHERE c.categoryId = ?1\n" +
                    "GROUP BY rp.productName, rp.product_count, c.categoryName";

            Query query = entityManager.createNativeQuery(hql);
            query.setParameter(1, categoryId);

            // Phân trang
            query.setFirstResult(offset);
            query.setMaxResults(limit);

            // Thực thi truy vấn
            List<Object[]> results = query.getResultList();

            for(Object[] row : results) {
                ProductDTO productDTO = new ProductDTO();
                String productName = (String) row[0];
                double price = (double) row[1];
                byte[] image = (byte[]) row[2];
                String description = (String) row[3];
                int quantity = ((Long) row[4]).intValue();

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
                                        .map(Product::toDTO)  // Sử dụng phương thức riêng cho việc ánh xạ sản phẩm
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList());
            } finally {
                entityManager.close();
            }
        }
    }
}
