package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IChatDAO;
import entity.Chat;
import entity.Customer;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Date;

public class ChatDAO implements IChatDAO {

    @Override
    public int getOrCreateChat(int userId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            // Bắt đầu giao dịch
            transaction.begin();

            // Truy vấn chatId của Customer hiện tại
            String jpql = "SELECT c.id FROM Chat c WHERE c.customer.userID = :userId ORDER BY c.createdDate DESC";
            TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
            query.setParameter("userId", userId);
            query.setMaxResults(1);

            // Lấy chatId nếu có, nếu không có thì tạo mới
            Integer chatId = query.getResultStream().findFirst().orElse(null);

            if (chatId == null) {
                Customer customer = new Customer();
                customer.setUserID(userId);

                // Nếu không có chat, tạo mới Chat và gán customerId vào
                Chat newChat = new Chat();
                newChat.setCustomer(customer); // Gán customerId vào chat mới
                newChat.setCreatedDate(new Date());

                entityManager.persist(newChat); // Lưu Chat mới vào cơ sở dữ liệu
                transaction.commit(); // Commit giao dịch

                chatId = newChat.getChatID(); // Lấy chatId mới tạo
            }

            return chatId; // Trả về chatId (có thể là chatId cũ hoặc mới)
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace();
            return -1; // Trả về null nếu có lỗi
        } finally {
            entityManager.close(); // Đóng EntityManager
        }
    }
}

