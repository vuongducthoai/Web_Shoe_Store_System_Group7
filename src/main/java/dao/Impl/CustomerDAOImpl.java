package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ICustomerDAO;
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
}
