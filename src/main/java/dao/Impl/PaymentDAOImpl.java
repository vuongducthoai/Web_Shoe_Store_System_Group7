package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IPaymentDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.Collections;
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
            int startYear = currentYear - 3; // Năm bắt đầu (cách đây 3 năm)

            // JPQL câu truy vấn
            String jpql = "SELECT FUNCTION('YEAR', py.paymentDate) AS year, " +
                    "FUNCTION('MONTH', py.paymentDate) AS month, " +
                    "COALESCE(SUM(py.amount), 0) AS total " +
                    "FROM Payment py " +
                    "JOIN py.order o " +
                    "WHERE o.orderStatus = 'COMPLETED' " +
                    "AND FUNCTION('YEAR', py.paymentDate) BETWEEN :startYear AND :currentYear " +
                    "GROUP BY FUNCTION('YEAR', py.paymentDate), FUNCTION('MONTH', py.paymentDate) " +
                    "ORDER BY FUNCTION('YEAR', py.paymentDate), FUNCTION('MONTH', py.paymentDate)";

            // Tạo truy vấn và gán tham số
            Query query = entityManager.createQuery(jpql);
            query.setParameter("startYear", startYear);
            query.setParameter("currentYear", currentYear);

            // Lấy kết quả
            List<Object[]> resultList = query.getResultList();

            // Tạo Map lưu doanh thu theo năm và tháng
            Map<Integer, Map<Integer, Long>> revenueMap = new HashMap<>();

            // Xử lý kết quả trả về
            for (Object[] result : resultList) {
                int year = ((Number) result[0]).intValue();
                int month = ((Number) result[1]).intValue();
                long totalAmount = ((Number) result[2]).longValue();

                // Thêm dữ liệu vào map
                revenueMap
                        .computeIfAbsent(year, k -> new HashMap<>())
                        .put(month, totalAmount);
            }

            // Đảm bảo rằng mỗi năm có đủ 12 tháng với doanh thu mặc định = 0
            for (int year = startYear; year <= currentYear; year++) {
                Map<Integer, Long> monthlyData = revenueMap.computeIfAbsent(year, k -> new HashMap<>());
                for (int month = 1; month <= 12; month++) {
                    monthlyData.putIfAbsent(month, 0L);
                }
            }

            return revenueMap;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap(); // Trả về Map rỗng nếu xảy ra lỗi
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close(); // Đóng EntityManager để tránh rò rỉ tài nguyên
            }
        }
    }






}
