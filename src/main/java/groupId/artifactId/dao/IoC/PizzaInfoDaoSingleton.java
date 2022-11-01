package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.exceptions.IncorrectDataSourceException;

import java.beans.PropertyVetoException;

public class PizzaInfoDaoSingleton {
    private volatile static PizzaInfoDaoSingleton firstInstance = null;
    private final IPizzaInfoDao pizzaInfoDao;

    public PizzaInfoDaoSingleton() {
        try {
            this.pizzaInfoDao = new PizzaInfoDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new IncorrectDataSourceException("Unable to get Data Source class for PizzaInfoDao", e);
        }
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
