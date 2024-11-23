package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IResponseDAO;
import entity.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ResponseDAOImpl implements IResponseDAO {
    public boolean addResponse(Response response) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try{
            transaction.begin();
            entityManager.persist(response);
            transaction.commit();
            return true;
        }catch(Exception e){
            e.printStackTrace();

            if(transaction.isActive()){
                transaction.rollback();
            }
        }finally{
            entityManager.close();
        }
        return false;
    }
}
