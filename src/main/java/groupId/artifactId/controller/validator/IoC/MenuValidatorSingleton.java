package groupId.artifactId.controller.validator.IoC;

import groupId.artifactId.controller.validator.MenuValidator;
import groupId.artifactId.controller.validator.api.IMenuValidator;

public class MenuValidatorSingleton {
    private final IMenuValidator menuValidator;
    private volatile static MenuValidatorSingleton firstInstance = null;

    public MenuValidatorSingleton() {
        this.menuValidator = new MenuValidator();
    }

    public static IMenuValidator getInstance() {
        synchronized (MenuValidatorSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuValidatorSingleton();
            }
            return firstInstance.menuValidator;
        }
    }
}
