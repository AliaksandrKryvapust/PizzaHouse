package groupId.artifactId.controller.validator.IoC;

import groupId.artifactId.controller.validator.OrderDataValidator;
import groupId.artifactId.controller.validator.api.IOrderDataValidator;

public class OrderDataValidatorSingleton {
    private final IOrderDataValidator validator;
    private volatile static OrderDataValidatorSingleton firstInstance = null;

    public OrderDataValidatorSingleton() {
        this.validator = new OrderDataValidator();
    }

    public static IOrderDataValidator getInstance() {
        synchronized (OrderDataValidatorSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new OrderDataValidatorSingleton();
            }
            return firstInstance.validator;
        }
    }
}
