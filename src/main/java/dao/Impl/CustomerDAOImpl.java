package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
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
        try{
            transaction.begin();
            entityManager.merge(customer);
            transaction.commit();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction.isActive()){
                transaction.rollback();
            }
        }finally {
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
            return false;
        }
    }

}
