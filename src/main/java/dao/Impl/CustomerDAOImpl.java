package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
import dto.CustomerDTO;
import entity.Account;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

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
}
