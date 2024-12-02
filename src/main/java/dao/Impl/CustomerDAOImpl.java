package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
import dto.CustomerDTO;
import dto.ChatDTO;
import entity.Account;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;

public class CustomerDAOImpl implements ICustomerDAO {

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
    public boolean findAccountByEmail(String email) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.email = :email";
            Account account = entityManager.createQuery(jpql, Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return account != null;
        } catch (NoResultException e) {
            return false; // Không tìm thấy account
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Customer getCustomerById(int userID) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            // Dùng JOIN FETCH để tải Customer kèm Address
            String jpql = "SELECT c FROM Customer c LEFT JOIN FETCH c.address WHERE c.userID = :userID";
            return entityManager.createQuery(jpql, Customer.class)
                    .setParameter("userID", userID)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Không tìm thấy customer
        } finally {
            entityManager.close();
        }
    }


    @Override
    public boolean updateCustomerByID(Customer customer) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Tìm khách hàng dựa trên userID
            Customer existingCustomer = entityManager.find(Customer.class, customer.getUserID());
            if (existingCustomer == null) {
                return false; // Khách hàng không tồn tại
            }

            // Cập nhật thông tin khách hàng
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setDateOfBirth(customer.getDateOfBirth());
            existingCustomer.setActive(customer.isActive());

            if (customer.getAddress() != null) {
                existingCustomer.setAddress(customer.getAddress());
            }

            // merge không cần thiết vì find trả về entity được quản lý
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } finally {
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


}
