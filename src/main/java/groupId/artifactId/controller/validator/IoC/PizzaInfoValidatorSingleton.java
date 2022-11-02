package groupId.artifactId.controller.validator.IoC;

import groupId.artifactId.controller.validator.PizzaInfoValidator;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;

public class PizzaInfoValidatorSingleton {
    private final IPizzaInfoValidator pizzaInfoValidator;
    private volatile static PizzaInfoValidatorSingleton firstInstance = null;

    public PizzaInfoValidatorSingleton() {
        this.pizzaInfoValidator = new PizzaInfoValidator();
    }

    public static IPizzaInfoValidator getInstance() {
        synchronized (PizzaInfoValidatorSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoValidatorSingleton();
            }
            return firstInstance.pizzaInfoValidator;
        }
    }
}
