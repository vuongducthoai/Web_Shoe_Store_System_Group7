import JpaConfig.JPAConfig;
import jakarta.persistence.EntityManager;

public class MAIN {
    public static void main(String[] args) {

        EntityManager em = JPAConfig.getEmFactory().createEntityManager();
            em.close();
    }
}
