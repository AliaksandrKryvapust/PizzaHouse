package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.OrderStageDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IOrderStageDao;

import java.beans.PropertyVetoException;

public class OrderStageDaoSingleton {
    private final IOrderStageDao orderStageDao;
    private volatile static OrderStageDaoSingleton instance=null;

    public OrderStageDaoSingleton() {
        try {
            this.orderStageDao = new OrderStageDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for OrderStageDao", e);
        }
    }

    public static IOrderStageDao getInstance(){
        synchronized (OrderStageDaoSingleton.class){
            if (instance==null){
                instance = new OrderStageDaoSingleton();
            }
        }
        return instance.orderStageDao;
    }
}
