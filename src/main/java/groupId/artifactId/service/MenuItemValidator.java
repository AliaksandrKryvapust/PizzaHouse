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

    }

    @Override
    public void validateMenuItem(MenuItemDto menuItemDto) {

    }
}
