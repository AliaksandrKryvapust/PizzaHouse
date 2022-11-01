package groupId.artifactId.service.IoC;

import groupId.artifactId.dao.IoC.MenuDaoSingleton;
import groupId.artifactId.service.MenuService;
import groupId.artifactId.service.api.IMenuService;

public class MenuServiceSingleton {
    private final IMenuService menuService;
    private volatile static MenuServiceSingleton firstInstance = null;

    public MenuServiceSingleton() {
        this.menuService = new MenuService(MenuDaoSingleton.getInstance());
    }

    public static IMenuService getInstance() {
        synchronized (MenuServiceSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuServiceSingleton();
            }
        }
        return firstInstance.menuService;
    }
}
