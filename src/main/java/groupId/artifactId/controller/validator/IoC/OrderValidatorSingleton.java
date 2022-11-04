package groupId.artifactId.controller.validator.IoC;

import groupId.artifactId.controller.validator.OrderValidator;
import groupId.artifactId.controller.validator.api.IOrderValidator;

public class OrderValidatorSingleton {
    private final IOrderValidator validator;
    private static OrderValidatorSingleton firstInstance = null;

    public OrderValidatorSingleton() {
        this.validator = new OrderValidator();
    }

    public static IOrderValidator getInstance() {
        synchronized (OrderValidatorSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new OrderValidatorSingleton();
            }
            return firstInstance.validator;
        }
    }
}
