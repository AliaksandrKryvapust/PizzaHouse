package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.service.api.IMenuItemValidator;

import java.util.List;

public class MenuItemValidator implements IMenuItemValidator {
    private static MenuItemValidator firstInstance = null;

    public static MenuItemValidator getInstance() {
        synchronized (MenuItemValidator.class) {
            if (firstInstance == null) {
                firstInstance = new MenuItemValidator();
            }
            return firstInstance;
        }
    }
    @Override
    public void validateListMenuItems(List<MenuItemDto> menuItemDto) {
        PizzaInfoValidator validator = PizzaInfoValidator.getInstance();
        for (MenuItemDto dto : menuItemDto) {
            if (dto.getInfo() == null) {
                throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
            }
            if (dto.getPrice() <= 0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
            }
            validator.validatePizzaInfo(dto.getInfo());
        }
    }

    @Override
    public void validateMenuItem(MenuItemDto menuItemDto) {
        if (menuItemDto.getInfo() == null) {
            throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
        }
        if (menuItemDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
        }
        PizzaInfoValidator validator = PizzaInfoValidator.getInstance();
        validator.validatePizzaInfo(menuItemDto.getInfo());
    }
}
