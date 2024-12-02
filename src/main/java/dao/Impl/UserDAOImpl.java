package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IUserDAO;
import entity.Account;
import entity.User;
import enums.RoleType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

public class UserDAOImpl implements IUserDAO {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public User getUserByAccountId(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        User user = null;
        try {
            String sql = "SELECT u.userID, a.role,u.active FROM User u " +
                    "INNER JOIN Account a ON u.accountID = a.accountID " +
                    "WHERE u.accountID = ?1";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, accountID);

            Object[] result = (Object[]) query.getSingleResult();

            int userId = (int) result[0];
            String role = (String) result[1];
            Boolean active = (Boolean) result[2];
            RoleType role1 = RoleType.valueOf(role);
            user = new User();
            user.setUserID(userId);
            user.setActive(active);
            Account account = new Account();
            account.setRole(role1);
            user.setAccount(account);
        } catch (NoResultException e) {
            System.out.println("Không tìm thấy User với accountID: " + accountID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return user;
    }

    public boolean checkAdmin(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String sql = "Select * from Admin inner join User on Admin.userID = User.userID where User.userID = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, userID);
            Object[] result = (Object[]) query.getSingleResult();

            if (result != null){
                System.out.print("co admin");
                return true;
            }else System.out.print("ko admin");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return false;
    }




}
