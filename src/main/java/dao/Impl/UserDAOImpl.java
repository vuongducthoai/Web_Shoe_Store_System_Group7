package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IUserDAO;
import entity.Account;
import entity.User;
import jakarta.persistence.EntityManager;

public class UserDAOImpl implements IUserDAO {

    @Override
    public User getUserByAccountId(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.account.accountID = :id";
            User result = entityManager.createQuery(jpql, User.class)
                    .setParameter("id", accountID)
                    .getSingleResult();
            return result;
        } catch (jakarta.persistence.NoResultException e) {
            // Nếu không tìm thấy kết quả, trả về null
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }
}
