package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuItemValidator implements IMenuItemValidator {
    private final IPizzaInfoValidator pizzaInfoValidator;

    @Autowired
    public MenuItemValidator(IPizzaInfoValidator pizzaInfoValidator) {
        this.pizzaInfoValidator = pizzaInfoValidator;
    }

    @Override
    public void validate(MenuItemDtoInput menuItemDtoInput) {
        pizzaInfoValidator.validate(menuItemDtoInput.getPizzaInfoDtoInput());
        if (menuItemDtoInput.getPrice() <= 0) {
            throw new IllegalArgumentException("MenuItem`s price is not valid");
        }
    }
}
