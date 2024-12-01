package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ITokenForgetPw;
import entity.Account;
import entity.TokenForgetPassword;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;


public class TokenForgetPw implements ITokenForgetPw {
    @Override
    public boolean insertToken(TokenForgetPassword token) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.merge(token);
            transaction.commit();
            return true;
        }catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally{
            entityManager.close();
        }
        return false;
    }

    @Override
    public TokenForgetPassword getTokenPassword(String token) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "SELECT t FROM TokenForgetPassword t WHERE t.token=:token";
            TokenForgetPassword tokenForgetPassword = entityManager.createQuery(jpql, TokenForgetPassword.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return tokenForgetPassword;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    @Override
    public boolean updateTokenPassword(TokenForgetPassword tokenForgetPassword) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Account account = tokenForgetPassword.getAccount();
            if(account != null){
                //Cap nhat mat khau trong account
                Account existingAccount = entityManager.find(Account.class, account.getAccountID());
                if(existingAccount != null){
                    existingAccount.setPassword(account.getPassword());
                    entityManager.merge(existingAccount);
                    tokenForgetPassword.setAccount(existingAccount);
                }
            }
            //Merge the token  entity to update it in the database
            entityManager.merge(tokenForgetPassword);
            transaction.commit();
            return true;
        } catch (Exception e){
            if(transaction != null && transaction.isActive()){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }
}
