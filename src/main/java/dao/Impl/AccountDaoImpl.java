package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAccountDAO;
import dto.AccountDTO;
import dto.UserDTO;
import entity.Account;
import entity.User;
import enums.AuthProvider;
import jakarta.persistence.*;

import java.util.*;

public class AccountDaoImpl implements IAccountDAO {
    private EntityManager entityManager;

    public AccountDaoImpl() {
        this.entityManager = JpaConfig.getEmFactory().createEntityManager();
    }
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
    public AccountDTO findAccountForLogin(AccountDTO accountDTO) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        AccountDTO accountDTO1 = null;
        try{
            String sql = "SELECT a.accountID FROM Account a WHERE a.email = ?1 AND a.password = ?2";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, accountDTO.getEmail());
            query.setParameter(2, accountDTO.getPassword());
            // Lấy kết quả trả về là Integer (vì chỉ một cột được chọn)
            Integer accountId = (Integer) query.getSingleResult();

            // Khởi tạo AccountDTO mới với giá trị trả về
            accountDTO1 = new AccountDTO();
            accountDTO1.setAccountID(accountId);
        }catch (NoResultException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return accountDTO1;
    }


    @Override
    public Account findAccountByEmail(String email) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.email = :email ";
            Account result = entityManager.createQuery(jpql, Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return result;
        } catch (Exception e) {
//            e.printStackTrace();
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
    @Override
    public List<AccountDTO> getListAccountDTO() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String hql = "SELECT p FROM Account p";
            TypedQuery<Account> query = entityManager.createQuery(hql, Account.class);
            List<Account> accountList = query.getResultList();


            List<AccountDTO> accountDTOList = new ArrayList<>();
            for (Account account : accountList) {
                System.out.println("Xử lý Account ID: " + account.getAccountID());

                User user = account.getUser();
                UserDTO userDTO = null;
                if (user != null) {
                    System.out.println("Tìm thấy User cho Account ID: " + account.getAccountID());
                    userDTO = new UserDTO(user.getUserID(), user.getFullName(), user.getPhone(), user.isActive(), null, null);
                }

                AccountDTO accountDTO = new AccountDTO(
                        account.getAccountID(),
                        account.getEmail(),
                        account.getPassword(),
                        account.getAuthProvider(),
                        account.getProviderID(),
                        account.getRole(),
                        userDTO
                );

                accountDTOList.add(accountDTO);
            }

            return accountDTOList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }
    public boolean updateAccount(AccountDTO accountDTO) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Tìm tài khoản theo accountID
            Account existingAccount = entityManager.find(Account.class, accountDTO.getAccountID());
            if (existingAccount == null) {
                // Nếu không tìm thấy tài khoản với accountID, trả về false
                return false;
            }

            // Cập nhật thông tin tài khoản với dữ liệu từ AccountDTO
            existingAccount.setEmail(accountDTO.getEmail());
            existingAccount.setPassword(accountDTO.getPassword());
            existingAccount.setAuthProvider(accountDTO.getAuthProvider());
            existingAccount.setProviderID(accountDTO.getProviderID());
            existingAccount.setRole(accountDTO.getRole());

            // Nếu có UserDTO trong AccountDTO, cập nhật thông tin user
            if (accountDTO.getUser() != null) {
                UserDTO userDTO = accountDTO.getUser();
                User existingUser = existingAccount.getUser();

                // Cập nhật thông tin user
                existingUser.setFullName(userDTO.getFullName());
                existingUser.setPhone(userDTO.getPhone());
                existingUser.setActive(userDTO.isActive());
            }

            // Persist the changes (Lưu lại các thay đổi)
            entityManager.merge(existingAccount);

            // Commit transaction
            transaction.commit();
            return true;  // Thành công
        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;  // Lỗi trong quá trình cập nhật
        }
    }
    public boolean updateAccountActive(int accountID, int status) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Tìm tài khoản theo accountID
            Account existingAccount = entityManager.find(Account.class, accountID);
            if (existingAccount == null) {
                // Nếu không tìm thấy tài khoản với accountID, trả về false
                return false;
            }

            // Cập nhật trạng thái active của user (không phải của account)
            User user = existingAccount.getUser();
            if (user != null) {
                user.setActive(status == 1);  // Chuyển 1 thành true, 0 thành false
            } else {
                // Nếu không có user liên kết, có thể trả về false hoặc xử lý thêm.
                return false;
            }

            // Persist the changes (Lưu lại các thay đổi)
            entityManager.merge(existingAccount);  // Lưu tài khoản (bao gồm cả user nếu có thay đổi)

            // Commit transaction
            transaction.commit();
            return true;  // Thành công
        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;  // Lỗi trong quá trình cập nhật
        }
    }


}


