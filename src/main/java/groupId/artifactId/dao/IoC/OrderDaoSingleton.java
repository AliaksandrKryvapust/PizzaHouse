package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.OrderDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IOrderDao;

import java.beans.PropertyVetoException;

public class OrderDaoSingleton {
    private final IOrderDao orderDao;
    private volatile static OrderDaoSingleton instance = null;

    public OrderDaoSingleton() {
        try {
            this.orderDao = new OrderDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for OrderDao", e);
        }
    }

    public static IOrderDao getInstance() {
        synchronized (OrderDaoSingleton.class) {
            if (instance == null) {
                instance = new OrderDaoSingleton();
            }
        }
        return instance.orderDao;
    }
}
