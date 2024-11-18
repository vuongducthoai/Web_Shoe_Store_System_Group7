package dao.Impl;

import JpaConfig.JpaConfig;
import dao.ITokenForgetPw;
import entity.TokenForgetPassword;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;


public class TokenForgetPw implements ITokenForgetPw {
    @Override
    public boolean insertToken(TokenForgetPassword token) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(token);
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

}
