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
    public Chat getOrCreateChat(int userId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Chat chat = null;
        try {
            transaction.begin();
            // Tìm User với userId
            Customer customer = entityManager.find(Customer.class, userId);
            if (customer == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }

            // Kiểm tra xem đã tồn tại Chat cho user chưa
            String jpql = "SELECT c FROM Chat c WHERE c.customer = :user ORDER BY c.createdDate DESC";
            TypedQuery<Chat> query = entityManager.createQuery(jpql, Chat.class);
            query.setParameter("user", customer);
            query.setMaxResults(1);

            Chat existingChat = query.getResultStream().findFirst().orElse(null);

            if (existingChat != null) {
                // Chat đã tồn tại
                chat = existingChat;
            } else {
                // Tạo mới Chat
                Chat newChat = new Chat();
                newChat.setCustomer(customer); // Gán đối tượng User vào Chat
                newChat.setCreatedDate(new Date());

                entityManager.persist(newChat); // Lưu vào database
                transaction.commit(); // Lưu thay đổi
                chat = newChat; // Lấy đối tượng Chat vừa tạo
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return chat;
    }
}
