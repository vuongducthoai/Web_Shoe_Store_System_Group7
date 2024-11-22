package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IProductDAO;
import entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductDAOImpl implements IProductDAO {

    @Override
    public List<Product> findAllWithPagination(int offset, int limit) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL để lấy tất cả các sản phẩm
            String query = "SELECT p FROM Product p";
            TypedQuery<Product> typedQuery  = entityManager.createQuery(query, Product.class);

            //Tang gioi hạn để láy thêm dữ liệu (gấp 3 limit)
            int fetchSize = limit * 2;

            // Phân trang
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(fetchSize);

            // Thực thi truy vấn
            List<Product> products = typedQuery.getResultList();

            // Loại bỏ các sản phẩm trùng tên (giữ lại 1 sản phẩm cho mỗi tên)
            Map<String, Product> uniqueProductsMap = new LinkedHashMap<>();
            for (Product product : products) {
                uniqueProductsMap.putIfAbsent(product.getProductName(), product);
            }

            List<Product> uniqueProducts = new ArrayList<>(uniqueProductsMap.values());
            return uniqueProducts.size() > limit ? uniqueProducts.subList(0, limit) // lay dung so luong
                                : uniqueProducts;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return null;
    }

    @Override
    public int countProductName(String name) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String query = "SELECT COUNT(p) FROM Product p WHERE p.productName = :productName AND p.status = false";
            TypedQuery<Long> typedQuery = entityManager.createQuery(query, Long.class);
            typedQuery.setParameter("productName", name);
            Long result = typedQuery.getSingleResult();
            return result.intValue();
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
