package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IOrderItemDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderItemDAOImpl implements IOrderItemDAO {
    @Override
    public Map<String, Integer> findTop10Products() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT p.productName, SUM(oi.quantity) " +
                    "FROM Order o " +
                    "JOIN o.orderItems oi " +
                    "JOIN oi.product p " +
                    "WHERE o.orderStatus = 'COMPLETED' " +
                    "GROUP BY p.productName " +
                    "ORDER BY SUM(oi.quantity) DESC";

            Query query = entityManager.createQuery(jpql);
            query.setMaxResults(10);  // Giới hạn 10 sản phẩm bán chạy nhất

            List<Object[]> resultList = query.getResultList();

            Map<String, Integer> topProducts = new LinkedHashMap<>();
            for (Object[] result : resultList) {
                String productName = (String) result[0];
                int totalQuantity = ((Number) result[1]).intValue();
                topProducts.put(productName, totalQuantity);

            }

            return topProducts;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();  // Trả về Map rỗng nếu có lỗi
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();  // Đảm bảo đóng EntityManager
                System.out.println("EntityManager closed.");
            }
        }
    }


}
