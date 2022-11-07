package groupId.artifactId.dao.IoC;

import groupId.artifactId.dao.PizzaDao;
import groupId.artifactId.dao.api.DataSourceCreator;
import groupId.artifactId.dao.api.IPizzaDao;

import java.beans.PropertyVetoException;

public class PizzaDaoSingleton {
    private final IPizzaDao pizzaDao;
    private volatile static PizzaDaoSingleton instance = null;

    public PizzaDaoSingleton() {
        try {
            this.pizzaDao = new PizzaDao(DataSourceCreator.getInstance());
        } catch (PropertyVetoException e) {
            throw new RuntimeException("Unable to get Data Source class for PizzaDao", e);
        }
    }

    public static IPizzaDao getInstance() {
        synchronized (PizzaDaoSingleton.class) {
            if (instance == null) {
                instance = new PizzaDaoSingleton();
            }
        }
        return instance.pizzaDao;
    }
}
