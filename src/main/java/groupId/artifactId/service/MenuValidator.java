package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.service.api.IMenuValidator;

import java.util.List;

public class MenuValidator implements IMenuValidator {
    private static MenuValidator firstInstance = null;

    public static MenuValidator getInstance() {
        synchronized (MenuValidator.class) {
            if (firstInstance == null) {
                firstInstance = new MenuValidator();
            }
            return firstInstance;
        }
    }

    @Override
    public void validateListMenuItems(List<MenuItemDto> menuItemDto) {
        MenuItemValidator itemValidator = MenuItemValidator.getInstance();
        itemValidator.validateListMenuItems(menuItemDto);
    }

    @Override
    public void validateMenuItem(MenuItemDto menuItemDto) {
        MenuItemValidator itemValidator = MenuItemValidator.getInstance();
        itemValidator.validateMenuItem(menuItemDto);
    }

    @Override
    public void validateMenu(MenuDto menuDto) {
        if (!MenuService.getInstance().isIdValid(menuDto.getId())) {
            throw new IllegalArgumentException("Error code 400. Menu with such id do not exist");
        }
        this.validateListMenuItems(menuDto.getItems());
    }
}
