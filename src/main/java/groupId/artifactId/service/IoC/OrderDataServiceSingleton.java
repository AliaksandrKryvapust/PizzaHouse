package groupId.artifactId.service.IoC;

import groupId.artifactId.dao.IoC.OrderDataDaoSingleton;
import groupId.artifactId.dao.IoC.OrderStageDaoSingleton;
import groupId.artifactId.service.OrderDataService;
import groupId.artifactId.service.api.IOrderDataService;

public class OrderDataServiceSingleton {
    private final IOrderDataService orderDataService;
    private volatile static OrderDataServiceSingleton firstInstance = null;

    public OrderDataServiceSingleton() {
        this.orderDataService = new OrderDataService(OrderDataDaoSingleton.getInstance(), OrderStageDaoSingleton.getInstance(),
                CompletedOrderServiceSingleton.getInstance());
    }

    public static IOrderDataService getInstance() {
        synchronized (OrderDataServiceSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new OrderDataServiceSingleton();
            }
        }
        return firstInstance.orderDataService;
    }
}
