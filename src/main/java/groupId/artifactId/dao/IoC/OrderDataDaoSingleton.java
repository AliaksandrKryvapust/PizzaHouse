package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.OrderDataDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IOrderDataDao;

public class OrderDataDaoSingleton {
    private final IOrderDataDao orderDataDao;
    private volatile static OrderDataDaoSingleton instance = null;

    public OrderDataDaoSingleton() {
        this.orderDataDao = new OrderDataDao(EntityManagerFactoryHibernate.getEntityManager());
    }

    public static IOrderDataDao getInstance() {
        synchronized (OrderDataDaoSingleton.class) {
            if (instance == null) {
                instance = new OrderDataDaoSingleton();
            }
        }
        return instance.orderDataDao;
    }
}
