package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.controller.validator.api.IMenuValidator;
import groupId.artifactId.service.MenuService;

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
        if (!MenuService.getInstance().isIdValid(menuItemDto.getId())) {
            throw new IllegalArgumentException("Error code 400. Menu with such id do not exist");
        }
    }

    @Override
    public void validateMenu(MenuDtoInput menuDtoInput) {
//        if (!MenuService.getInstance().isIdValid(menuDtoInput.getId())) {
//            throw new IllegalArgumentException("Error code 400. Menu with such id do not exist");
//        }
        this.validateMenuRow(menuDtoInput);
    }

    @Override
    public void validateMenuRow(MenuDtoInput menuDtoInput) {
        if (menuDtoInput.getName() == null || menuDtoInput.getName().isBlank()) {
            throw new IllegalArgumentException("Menu`s name is not valid");
        }
        if (menuDtoInput.getEnable() == null) {
            throw new IllegalArgumentException("Menu`s enable status is not valid");
        }
    }

}
