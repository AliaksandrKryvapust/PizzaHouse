package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IMenuItemDao;

public class MenuItemDaoSingleton {
    private volatile static MenuItemDaoSingleton firstInstance = null;
    private final IMenuItemDao menuItemDao;

    public MenuItemDaoSingleton() {
        this.menuItemDao = new MenuItemDao(EntityManagerFactoryHibernate.getEntityManager());
    }

    public static IMenuItemDao getInstance() {
        synchronized (MenuItemDaoSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemDaoSingleton();
            }
        }
        return firstInstance.menuItemDao;
    }
}
