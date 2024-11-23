package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAccountDAO;
import dto.AccountDTO;
import entity.Account;
import enums.AuthProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AccountDaoImpl implements IAccountDAO {
    private EntityManager entityManager;

    @Override

    public boolean InsertAccount(Account account) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(account);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    @Override
    public boolean findAccountForLogin(Account account) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.email = :email AND a.password = :password";
            Account result = entityManager.createQuery(jpql, Account.class)
                    .setParameter("email", account.getEmail())
                    .setParameter("password", account.getPassword())
                    .getSingleResult();

            return result != null;
        } catch (jakarta.persistence.NoResultException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    @Override
    public Account findAccountByEmail(String email) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.email = :email AND a.authProvider = :authProvider";
            Account result = entityManager.createQuery(jpql, Account.class)
                    .setParameter("email", email)
                    .setParameter("authProvider", AuthProvider.LOCAL)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    @Override
    public Account getAccountByID(int accountID) {  // Sửa tên phương thức để đồng nhất với interface
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.accountID = :id";
            Account result = entityManager.createQuery(jpql, Account.class)
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

    @Override
    public Account findAccoutByProvide(String provideID, AuthProvider authProvider) {
        return null;
    }


    @Override
    public Account findAccountByProvide(String provideID, AuthProvider authProvider) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.providerID = :provideID AND a.authProvider = :authProvider";
            Account result = entityManager.createQuery(jpql, Account.class)
                    .setParameter("provideID", provideID)
                    .setParameter("authProvider", authProvider)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    // Update account by accountID
    public boolean updateAccountByAccountID(int accountID, AccountDTO accountDTO) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Find the existing account by accountID
            Account existingAccount = entityManager.find(Account.class, accountID);
            if (existingAccount == null) {
                // If no account found with given accountID, return false
                return false;
            }
            // Update the existing account with values from AccountDTO
            existingAccount.setEmail(accountDTO.getEmail());
            existingAccount.setPassword(accountDTO.getPassword());
            existingAccount.setAuthProvider(accountDTO.getAuthProvider());
            existingAccount.setProviderID(accountDTO.getProviderID());
            existingAccount.setRole(accountDTO.getRole());
            // Persist the changes
            entityManager.merge(existingAccount);

            // Commit the transaction
            transaction.commit();

            return true; // Successfully updated
        } catch (Exception e) {
            // Rollback transaction if an error occurs
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false; // Error occurred during update
        }
    }
}


