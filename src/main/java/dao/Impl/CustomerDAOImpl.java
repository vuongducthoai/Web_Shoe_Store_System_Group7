package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
import entity.Account;
import entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class CustomerDAOImpl implements ICustomerDAO {

    @Override
    public boolean insertCustomer(Customer customer) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(customer);
            transaction.commit();
            return true;
        }catch (Exception e){
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
            String jpql= "SELECT a FROM Account a where a.email = :email";
            Account result = entityManager.createQuery(jpql, Account.class).setParameter("email", email).getSingleResult();
            return result != null;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
        return false;
    }

}
