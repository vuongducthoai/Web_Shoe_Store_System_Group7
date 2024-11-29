package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IUserDAO;
import entity.Account;
import entity.User;
import enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class UserDAOImpl implements IUserDAO {

    @Override
    public User getUserByAccountId(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        User user = null;
        try {
            String sql = "SELECT u.userID, a.role FROM User u " +
                    "INNER JOIN Account a ON u.accountID = a.accountID " +
                    "WHERE u.accountID = ?1";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, accountID);

            Object[] result = (Object[]) query.getSingleResult();

            int userId = (int) result[0];
            String role = (String) result[1];

            user = new User();
            user.setUserID(userId);

            Account account = new Account();
            if(RoleType.CUSTOMER.equals(role)){
                account.setRole(RoleType.CUSTOMER);
            } else {
                account.setRole(RoleType.ADMIN);
            }
            user.setAccount(account);
        } catch (NoResultException e) {
            System.out.println("Không tìm thấy User với accountID: " + accountID);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return user;
    }


}
