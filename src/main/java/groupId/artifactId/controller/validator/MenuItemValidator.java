package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IMenuItemValidator;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;

public class MenuItemValidator implements IMenuItemValidator {

    public MenuItemValidator() {
    }

    @Override
    public void validate(MenuItemDtoInput menuItemDtoInput) {
        if (menuItemDtoInput.getPrice() <= 0) {
            throw new IllegalArgumentException("MenuItem`s price is not valid");
        }
        if (menuItemDtoInput.getPizzaInfoId() <= 0) {
            throw new IllegalArgumentException("MenuItem`s pizza info id is not valid");
        }
        if (menuItemDtoInput.getMenuId() <= 0) {
            throw new IllegalArgumentException("MenuItem`s menu id price is not valid");
        }
    }
}
