package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import java.beans.PropertyVetoException;

public class MenuDaoSingleton {
    private final IMenuDao menuDao;
    private volatile static MenuDaoSingleton firstInstance = null;

    public MenuDaoSingleton() {
        try {
            this.menuDao = new MenuDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class for MenuDao", e);
        }
    }

    public static IMenuDao getInstance() {
        synchronized (MenuDaoSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuDaoSingleton();
            }
        }
        return firstInstance.menuDao;
    }
}
