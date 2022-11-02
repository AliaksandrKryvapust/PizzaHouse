package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.MenuItemDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IMenuItemDao;

import java.beans.PropertyVetoException;

public class MenuItemDaoSingleton {
    private volatile static MenuItemDaoSingleton firstInstance = null;
    private final IMenuItemDao menuItemDao;

    public MenuItemDaoSingleton() {
        try {
            this.menuItemDao = new MenuItemDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class at MenuItemDao", e);
        }
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
