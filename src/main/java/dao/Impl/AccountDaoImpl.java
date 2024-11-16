package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IAccountDAO;
import entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AccountDaoImpl implements IAccountDAO {

    @Override
    public boolean InsertAccount(Account account) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(account);
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

}
