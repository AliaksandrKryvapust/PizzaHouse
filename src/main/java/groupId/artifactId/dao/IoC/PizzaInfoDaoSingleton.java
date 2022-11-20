package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IPizzaInfoDao;

public class PizzaInfoDaoSingleton {
    private volatile static PizzaInfoDaoSingleton firstInstance = null;
    private final IPizzaInfoDao pizzaInfoDao;

    public PizzaInfoDaoSingleton() {
        this.pizzaInfoDao = new PizzaInfoDao(EntityManagerFactoryHibernate.getInstance());
    }

    public static IPizzaInfoDao getInstance() {
        synchronized (PizzaInfoDaoSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoDaoSingleton();
            }
        }
        return firstInstance.pizzaInfoDao;
    }
}
