package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.OrderDataDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IOrderDataDao;

import java.beans.PropertyVetoException;

public class OrderDataDaoSingleton {
    private final IOrderDataDao orderDataDao;
    private volatile static OrderDataDaoSingleton instance = null;

    public OrderDataDaoSingleton() {
        try {
            this.orderDataDao = new OrderDataDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for OrderDataDao", e);
        }
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
