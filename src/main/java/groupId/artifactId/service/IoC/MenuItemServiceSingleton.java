package groupId.artifactId.service.IoC;

import groupId.artifactId.dao.IoC.MenuItemDaoSingleton;
import groupId.artifactId.service.MenuItemService;
import groupId.artifactId.service.api.IMenuItemService;

public class MenuItemServiceSingleton {
    private final IMenuItemService menuItemService;
    private volatile static MenuItemServiceSingleton firstInstance = null;

    public MenuItemServiceSingleton() {
        this.menuItemService = new MenuItemService(MenuItemDaoSingleton.getInstance());
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
