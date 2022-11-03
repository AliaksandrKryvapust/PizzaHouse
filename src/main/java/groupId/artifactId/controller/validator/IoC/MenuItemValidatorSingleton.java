package groupId.artifactId.controller.validator.IoC;

import groupId.artifactId.controller.validator.MenuItemValidator;
import groupId.artifactId.controller.validator.api.IMenuItemValidator;

public class MenuItemValidatorSingleton {
    private final IMenuItemValidator menuItemValidator;
    private volatile static MenuItemValidatorSingleton firstInstance = null;

    public MenuItemValidatorSingleton() {
        this.menuItemValidator = new MenuItemValidator();
    }

    public static IMenuItemValidator getInstance() {
        synchronized (MenuItemValidatorSingleton.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemValidatorSingleton();
            }
            return firstInstance.menuItemValidator;
        }
    }
}
