package groupId.artifactId.service.IoC;

import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.IoC.MenuItemDaoSingleton;
import groupId.artifactId.dao.IoC.PizzaInfoDaoSingleton;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.service.MenuItemService;
import groupId.artifactId.service.api.IMenuItemService;

public class MenuItemServiceSingleton {
    private final IMenuItemService menuItemService;
    private volatile static MenuItemServiceSingleton firstInstance = null;

    public MenuItemServiceSingleton() {
        this.menuItemService = new MenuItemService(MenuItemDaoSingleton.getInstance(), PizzaInfoDaoSingleton.getInstance(),
                new MenuItemMapper(new PizzaInfoMapper()), EntityManagerFactoryHibernate.getEntityManager());
    }

    public static IMenuItemService getInstance() {
        synchronized (MenuItemServiceSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemServiceSingleton();
            }
        }
        return firstInstance.menuItemService;
    }
}
