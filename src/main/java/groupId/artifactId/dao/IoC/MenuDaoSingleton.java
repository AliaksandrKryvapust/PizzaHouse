package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.MenuDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IMenuDao;

public class MenuDaoSingleton {
    private final IMenuDao menuDao;
    private volatile static MenuDaoSingleton firstInstance = null;

    public MenuDaoSingleton() {
        this.menuDao = new MenuDao(EntityManagerFactoryHibernate.getEntityManager());
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
