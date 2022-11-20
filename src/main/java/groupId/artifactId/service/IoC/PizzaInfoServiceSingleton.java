package groupId.artifactId.service.IoC;

import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.IoC.PizzaInfoDaoSingleton;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.service.PizzaInfoService;
import groupId.artifactId.service.api.IPizzaInfoService;

public class PizzaInfoServiceSingleton {
    private final IPizzaInfoService pizzaInfoService;
    private volatile static PizzaInfoServiceSingleton firstInstance = null;

    public PizzaInfoServiceSingleton() {
        this.pizzaInfoService = new PizzaInfoService(PizzaInfoDaoSingleton.getInstance(), new PizzaInfoMapper(),
                EntityManagerFactoryHibernate.getInstance());
    }

    public static IPizzaInfoService getInstance() {
        synchronized (PizzaInfoServiceSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoServiceSingleton();
            }
        }
        return firstInstance.pizzaInfoService;
    }
}
