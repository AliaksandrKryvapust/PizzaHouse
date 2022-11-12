package groupId.artifactId.service.IoC;

import groupId.artifactId.core.mapper.*;
import groupId.artifactId.dao.IoC.OrderDaoSingleton;
import groupId.artifactId.dao.IoC.SelectedItemDaoSingleton;
import groupId.artifactId.dao.IoC.TicketDaoSingleton;
import groupId.artifactId.service.OrderService;
import groupId.artifactId.service.api.IOrderService;

public class OrderServiceSingleton {
    private final IOrderService orderService;
    private volatile static OrderServiceSingleton instance = null;

    public OrderServiceSingleton() {
        this.orderService = new OrderService(SelectedItemDaoSingleton.getInstance(), OrderDaoSingleton.getInstance(),
                TicketDaoSingleton.getInstance(), OrderDataServiceSingleton.getInstance(), new TicketMapper(new OrderMapper(
                new SelectedItemMapper(new MenuItemMapper(new PizzaInfoMapper())))), new OrderMapper(new SelectedItemMapper(
                new MenuItemMapper(new PizzaInfoMapper()))));
    }

    public static IOrderService getInstance() {
        synchronized (OrderServiceSingleton.class) {
            if (instance == null) {
                instance = new OrderServiceSingleton();
            }
        }
        return instance.orderService;
    }
}
