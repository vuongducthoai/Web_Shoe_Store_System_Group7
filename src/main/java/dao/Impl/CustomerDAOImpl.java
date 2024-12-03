package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAccountDAO;
import dao.ICustomerDAO;
import dto.CustomerDTO;
import entity.Account;
import entity.Address;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.List;

public class CustomerDAOImpl implements ICustomerDAO {
    IAccountDAO accountDAO = new AccountDaoImpl();
    @Override
    public boolean insertCustomer(Customer customer) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(customer); // Sử dụng persist thay vì merge cho insert
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error inserting customer: " + e.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }
        return false;
    }

    @Override
    public Customer getCustomerByAccountID(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Truy vấn với JOIN FETCH để lấy dữ liệu liên kết
            String jpql = "SELECT c FROM Customer c LEFT JOIN FETCH c.address WHERE c.account.id = :accountID";
            Customer customer = entityManager.createQuery(jpql, Customer.class)
                    .setParameter("accountID", accountID)
                    .getSingleResult();

            // Chuyển đổi sang DTO nếu cần
            return (customer);
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }
    public Integer getUserIDByAccountId(int accountID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Dùng JPQL để truy vấn chỉ lấy userID của Customer
            String jpql = "SELECT c.userID FROM User c WHERE c.account.accountID = :accountID";
            return entityManager.createQuery(jpql, Integer.class)
                    .setParameter("accountID", accountID)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Nếu không tìm thấy customer
        } finally {
            entityManager.close();
        }
    }


    @Override
    public boolean updateCustomerByID(Customer customer) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            // Bắt đầu giao dịch
            transaction.begin();

            // Tìm customer cũ trong cơ sở dữ liệu
            Customer oldCustomer = entityManager.find(Customer.class, customer.getUserID());
            System.out.println("Customer "+  oldCustomer.getFullName());
            if (oldCustomer == null) {
                System.out.println("Customer not found with ID: " + customer.getUserID());
                return false;
            }

            // Cập nhật thông tin customer
            oldCustomer.setDateOfBirth(customer.getDateOfBirth());
            oldCustomer.setPhone(customer.getPhone());
            oldCustomer.setFullName(customer.getFullName());

            // Cập nhật địa chỉ
            Address address = customer.getAddress(); // Kiểm tra nếu customer đã có địa chỉ
            if (address != null) {
                oldCustomer.setAddress(address); // Cập nhật địa chỉ mới nếu có
            }

            // Commit giao dịch

            transaction.commit();
            return true;

        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Đóng EntityManager
            entityManager.close();
        }
    }


    @Override
    public List<CustomerDTO> GetAllCustomer() {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Query to fetch specific fields and map to CustomerDTO and ChatDTO
            String jpql = "SELECT new dto.CustomerDTO(c.userID, c.fullName, c.phone," +
                    "(SELECT m.isRead FROM Message m WHERE m.chat.chatID = c2_0.chatID AND m.userID = c1_1.userID ORDER BY m.date DESC LIMIT 1)) " +
                    "FROM Customer c " +
                    "JOIN User c1_1 ON c.userID = c1_1.userID " +
                    "JOIN Chat c2_0 ON c.userID = c2_0.customer.userID " +
                    "JOIN Message m1_0 ON c2_0.chatID = m1_0.chat.chatID " +
                    "WHERE c1_1.account.role = 'CUSTOMER'" +
                    "GROUP BY c.userID, c1_1.fullName, c1_1.phone, c2_0.chatID " +
                    "ORDER BY MAX(m1_0.date) DESC";



            // Execute the query to get the results and map them directly to CustomerDTO
            List<CustomerDTO> customerDTOList = entityManager.createQuery(jpql, CustomerDTO.class)
                    .getResultList();

            return customerDTOList;
        } catch (Exception e) {
            System.out.println("Error fetching customers: " + e.getMessage());
            return null;
        } finally {
            entityManager.close();
        }
    }

    public int getCustomerLoyaty(int userID){
        int result = 0;
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try{
            String sql = "select loyaty from Customer where userID=?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, userID);
            List<Object[]> results = query.getResultList();
            for (Object[] row : results) {
                int loyaty = (int) row[0];
                result = loyaty;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}
