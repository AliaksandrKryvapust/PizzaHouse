package groupId.artifactId.service.IoC;

import groupId.artifactId.core.mapper.*;
import groupId.artifactId.dao.IoC.OrderDataDaoSingleton;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.service.OrderDataService;
import groupId.artifactId.service.api.IOrderDataService;

public class OrderDataServiceSingleton {
    private final IOrderDataService orderDataService;
    private volatile static OrderDataServiceSingleton firstInstance = null;

    public OrderDataServiceSingleton() {
        this.orderDataService = new OrderDataService(OrderDataDaoSingleton.getInstance(),
                new OrderDataMapper(new OrderStageMapper(),
                        new TicketMapper(new OrderMapper(new SelectedItemMapper(new MenuItemMapper(new PizzaInfoMapper()))))),
                new OrderStageMapper(), EntityManagerFactoryHibernate.getEntityManager(),
                CompletedOrderServiceSingleton.getInstance(), new CompletedOrderMapper(new TicketMapper(
                        new OrderMapper(new SelectedItemMapper(new MenuItemMapper(new PizzaInfoMapper())))),
                new PizzaMapper()));
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
