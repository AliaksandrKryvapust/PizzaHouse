package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.CompletedOrderDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.ICompletedOrderDao;

import java.beans.PropertyVetoException;

public class CompletedOrderDaoSingleton {
    private final ICompletedOrderDao completedOrderDao;
    private volatile static CompletedOrderDaoSingleton instance = null;

    public CompletedOrderDaoSingleton() {
        try {
            this.completedOrderDao = new CompletedOrderDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for CompletedOrderDao", e);
        }
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

