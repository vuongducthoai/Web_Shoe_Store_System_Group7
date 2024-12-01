package dao.Impl;

import JpaConfig.JpaConfig;
import dao.IResponseDAO;
import entity.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ResponseDAOImpl implements IResponseDAO {
    public boolean addResponse(Response response) {
        if (response == null) {
            System.err.println("Response is null");
            return false;
        }

        try (EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                if (response.getContent() == null || response.getContent().trim().isEmpty()) {
                    System.err.println("Response content is empty");
                    return false;
                }
                if (response.getReview() == null || response.getReview().getReviewID() == 0) {
                    System.err.println("Review ID is invalid");
                    return false;
                }
                if (response.getAdmin() == null || response.getAdmin().getUserID() == 0) {
                    System.err.println("Admin/User ID is invalid");
                    return false;
                }

                // Lưu hoặc cập nhật thực thể
                entityManager.merge(response);
                transaction.commit();
                return true;

            } catch (Exception e) {
                System.err.println("Error while adding response: " + e.getMessage());
                e.printStackTrace();

                // Rollback nếu có lỗi
                if (transaction.isActive()) {
                    System.err.println("Rolling back transaction");
                    transaction.rollback();
                }
                return false;
            }
        }
    }

    public boolean deleteResponse(int responseID) {
        try(EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager()){
            EntityTransaction transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                Response response = entityManager.find(Response.class, responseID);
                if(response == null) return false;
                entityManager.remove(response);
                transaction.commit();
                return true;
            }catch(Exception e){
                e.printStackTrace();
                if(transaction.isActive()) transaction.rollback();
                return false;
            }
        }
    }

}
