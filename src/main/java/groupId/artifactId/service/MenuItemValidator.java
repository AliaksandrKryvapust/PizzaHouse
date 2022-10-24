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
        for (MenuItemDto dto : menuItemDto) {
            if (dto.getInfo() == null) {
                throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
            }
            if (dto.getInfo().getName() == null || dto.getInfo().getName().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
            }
            if (dto.getInfo().getDescription() == null || dto.getInfo().getDescription().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
            }
            if (dto.getInfo().getSize() <= 0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
            }
            if (dto.getPrice() <= 0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
            }
        }
    }

    @Override
    public void validateMenuItem(MenuItemDto menuItemDto) {
        if (menuItemDto.getInfo() == null) {
            throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
        }
        if (menuItemDto.getInfo().getName() == null || menuItemDto.getInfo().getName().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
        }
        if (menuItemDto.getInfo().getDescription() == null || menuItemDto.getInfo().getDescription().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
        }
        if (menuItemDto.getInfo().getSize() <= 0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
        }
        if (menuItemDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
        }
        if (!MenuService.getInstance().isIdValid(menuItemDto.getId())) {
            throw new IllegalArgumentException("Error code 400. Menu with such id do not exist");
        }
    }
}
