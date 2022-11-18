package groupId.artifactId.dao.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryHibernate {
    private static EntityManagerFactoryHibernate instance = null;
    private final EntityManager entityManager;

    public EntityManagerFactoryHibernate() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pizzeria");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public static EntityManager getInstance() {
        synchronized (EntityManagerFactoryHibernate.class) {
            if (instance == null) {
                instance = new EntityManagerFactoryHibernate();
            }
            return instance.entityManager;
        }
    }
}
