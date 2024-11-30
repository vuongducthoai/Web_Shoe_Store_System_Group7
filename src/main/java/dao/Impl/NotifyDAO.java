package dao.Impl;

import JpaConfig.JpaConfig;
import dao.INotifyDAO;
import dto.CustomerDTO;
import dto.NotifyDTO;
import entity.Notify;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.eclipse.tags.shaded.org.apache.bcel.generic.Select;

import java.util.List;

public class NotifyDAO implements INotifyDAO {
    @Override
    public List<NotifyDTO> LoadNotifies(int userId) {
        EntityManager entityManager = JpaConfig.getEmFactory().createEntityManager();
        try {
            String jpql = "select new dto.NotifyDTO(n.notifyID,n.content, n.timeStamp, n.user.userID) from Notify n where n.user.userID=:userId order by n.timeStamp DESC ";
            List<NotifyDTO> notifyList = entityManager.createQuery(jpql, NotifyDTO.class)
                    .setParameter("userId", userId)
                    .getResultList();
            return notifyList;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            entityManager.close();
        }
    }
}
