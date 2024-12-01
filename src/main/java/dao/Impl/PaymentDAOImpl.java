package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IPaymentDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDAOImpl implements IPaymentDAO {

        @Override
        public long totalAmount() {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            try {
                // JPQL câu truy vấn để tính tổng amount
                String jpql = "SELECT SUM(py.amount) FROM Payment py JOIN Order o ON py.order.orderId = o.orderId WHERE o.orderStatus='COMPLETED'";

                // Tạo truy vấn
                Query query = entityManager.createQuery(jpql);

                // Lấy kết quả
                Object result = query.getSingleResult();

                // Trả về kết quả hoặc 0 nếu kết quả là null
                return result != null ? ((Number) result).longValue() : 0L;
            } catch (Exception e) {
                // Xử lý lỗi (nếu có)
                e.printStackTrace();
                return 0L; // Trả về 0L nếu có lỗi
            } finally {
                // Đảm bảo EntityManager được đóng để tránh rò rỉ tài nguyên
                if (entityManager != null && entityManager.isOpen()) {
                    entityManager.close();
                }
            }
        }
    public long totalMonth() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // JPQL câu truy vấn để lấy doanh thu của tháng và năm hiện tại từ các đơn hàng 'COMPLETED'
            String jpql = "SELECT SUM(py.amount) FROM Payment py " +
                    "JOIN py.order o " + // Giả sử Payment có quan hệ với Order qua trường 'order'
                    "WHERE o.orderStatus = 'COMPLETED' " +
                    "AND FUNCTION('MONTH', py.paymentDate) = FUNCTION('MONTH', CURRENT_DATE) " +
                    "AND FUNCTION('YEAR', py.paymentDate) = FUNCTION('YEAR', CURRENT_DATE)";

            // Tạo truy vấn
            Query query = entityManager.createQuery(jpql);

            // Lấy kết quả
            Object result = query.getSingleResult();

            // Trả về kết quả hoặc 0 nếu kết quả là null, chuyển về kiểu long
            return result != null ? ((Number) result).longValue() : 0L;
        } catch (Exception e) {
            // Xử lý lỗi (nếu có)
            e.printStackTrace();
            return 0L; // Trả về 0 nếu có lỗi
        } finally {
            // Đảm bảo EntityManager được đóng để tránh rò rỉ tài nguyên
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    public Map<Integer, Map<Integer, Long>> totalRevenueForLastFourYears() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Tính năm hiện tại và năm 3 năm trước
            int currentYear = LocalDate.now().getYear();
            int startYear = currentYear - 3; // Lấy năm 3 năm trước
            int endYear = currentYear;       // Lấy năm hiện tại

            // JPQL câu truy vấn để lấy doanh thu của mỗi tháng trong 4 năm gần nhất
            String jpql = "SELECT FUNCTION('YEAR', py.paymentDate) AS year, " +
                    "FUNCTION('MONTH', py.paymentDate) AS month, " +
                    "COALESCE(SUM(py.amount), 0) AS total " +
                    "FROM Payment py " +
                    "JOIN py.order o " +
                    "WHERE o.orderStatus = 'COMPLETED' " +
                    "AND FUNCTION('YEAR', py.paymentDate) BETWEEN :startYear AND :endYear " +
                    "GROUP BY FUNCTION('YEAR', py.paymentDate), FUNCTION('MONTH', py.paymentDate) " +
                    "ORDER BY FUNCTION('YEAR', py.paymentDate), FUNCTION('MONTH', py.paymentDate)";


            // Tạo truy vấn và truyền tham số
            Query query = entityManager.createQuery(jpql);
            query.setParameter("startYear", startYear);
            query.setParameter("endYear", endYear);

            // Lấy kết quả
            List<Object[]> resultList = query.getResultList();

            // Tạo Map để lưu doanh thu theo năm và tháng
            Map<Integer, Map<Integer, Long>> resultMap = new HashMap<>();

            // Lặp qua kết quả trả về và thêm vào Map
            for (Object[] result : resultList) {
                int year = (int) result[0];
                int month = (int) result[1];
                long totalAmount = (long) result[2];

                resultMap
                        .computeIfAbsent(year, k -> new HashMap<>())
                        .put(month, totalAmount);
            }

            // Đảm bảo rằng mỗi năm có đủ 12 tháng, nếu thiếu thì thêm tháng với doanh thu = 0
            for (int year = startYear; year <= endYear; year++) {
                Map<Integer, Long> monthlyData = resultMap.computeIfAbsent(year, k -> new HashMap<>());
                for (int month = 1; month <= 12; month++) {
                    monthlyData.putIfAbsent(month, 0L); // Nếu tháng không có doanh thu, gán = 0
                }
            }

            return resultMap;
        } catch (Exception e) {
            // Xử lý lỗi (nếu có)
            e.printStackTrace();
            return new HashMap<>(); // Trả về Map rỗng nếu có lỗi
        } finally {
            // Đảm bảo EntityManager được đóng để tránh rò rỉ tài nguyên
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }





}
