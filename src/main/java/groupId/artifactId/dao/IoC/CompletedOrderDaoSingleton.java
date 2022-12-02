package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.CompletedOrderDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.ICompletedOrderDao;

public class CompletedOrderDaoSingleton {
    private final ICompletedOrderDao completedOrderDao;
    private volatile static CompletedOrderDaoSingleton instance = null;

    public CompletedOrderDaoSingleton() {
        this.completedOrderDao = new CompletedOrderDao(EntityManagerFactoryHibernate.getEntityManager());
    }

    public static ICompletedOrderDao getInstance() {
        synchronized (CompletedOrderDaoSingleton.class) {
            if (instance == null) {
                instance = new CompletedOrderDaoSingleton();
            }
        }
        return instance.completedOrderDao;
    }
}

