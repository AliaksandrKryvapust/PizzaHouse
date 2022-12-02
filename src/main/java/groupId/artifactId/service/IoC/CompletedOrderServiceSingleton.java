package groupId.artifactId.service.IoC;

import groupId.artifactId.core.mapper.*;
import groupId.artifactId.dao.IoC.CompletedOrderDaoSingleton;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.service.CompletedOrderService;
import groupId.artifactId.service.api.ICompletedOrderService;

public class CompletedOrderServiceSingleton {
    private final ICompletedOrderService completedOrderService;
    private volatile static CompletedOrderServiceSingleton firstInstance = null;

    public CompletedOrderServiceSingleton() {
        this.completedOrderService = new CompletedOrderService(CompletedOrderDaoSingleton.getInstance(),
                new CompletedOrderMapper(new TicketMapper(new OrderMapper(
                new SelectedItemMapper(new MenuItemMapper(new PizzaInfoMapper())))), new PizzaMapper()),
                EntityManagerFactoryHibernate.getEntityManager());
    }

    public static ICompletedOrderService getInstance() {
        synchronized (CompletedOrderServiceSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new CompletedOrderServiceSingleton();
            }
        }
        return firstInstance.completedOrderService;
    }
}
