package dao.Impl;
import JpaConfig.JpaConfig;
import dao.IPaymentDAO;
import entity.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
public class PaymentDAOImpl implements IPaymentDAO {

        @Override
        public long totalAmount() {
            EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
            try {
                // JPQL câu truy vấn để tính tổng amount
                String jpql = "SELECT SUM(py.amount) FROM Payment py JOIN Order o ON py.id = o.orderId WHERE o.orderStatus='COMPLETED'";

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



}
