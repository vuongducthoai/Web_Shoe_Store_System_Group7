package JpaConfig;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ShoeStore");

    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}

