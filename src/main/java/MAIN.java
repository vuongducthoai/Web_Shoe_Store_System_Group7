import JpaConfig.JpaConfig;
import jakarta.persistence.EntityManager;

public class MAIN {
    public static void main(String[] args) {
        EntityManager em = JpaConfig.getEmFactory().createEntityManager();
            em.close();
    }
}
