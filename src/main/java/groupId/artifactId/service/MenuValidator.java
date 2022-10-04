package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.service.api.IMenuValidator;

import java.util.List;

public class MenuValidator implements IMenuValidator {
    private static MenuValidator firstInstance=null;

    public static MenuValidator getInstance(){
        synchronized (MenuValidator.class){
            if (firstInstance==null){
                firstInstance=new MenuValidator();
            }
            return firstInstance;
        }
    }
    @Override
    public void validateMenu(List<MenuItemDto> menuItemDto) {
        for (MenuItemDto dto: menuItemDto ) {
            if (dto.getInfo() == null) {
                throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
            }
            if (dto.getInfo().getName() == null || dto.getInfo().getName().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
            }
            if (dto.getInfo().getDescription() == null || dto.getInfo().getDescription().isBlank()) {
                throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
            }
            if (dto.getInfo().getSize() <=0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
            }
            if (dto.getPrice() <=0) {
                throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
            }
        }
    }

    @Override
    public void validateMenuItem(MenuItemDtoWithId menuItemDtoWithId) {
        if (menuItemDtoWithId.getInfo() == null) {
            throw new IllegalStateException("Error code 500. None of MenuItem have been sent as an input");
        }
        if (menuItemDtoWithId.getInfo().getName() == null || menuItemDtoWithId.getInfo().getName().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
        }
        if (menuItemDtoWithId.getInfo().getDescription() == null || menuItemDtoWithId.getInfo().getDescription().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
        }
        if (menuItemDtoWithId.getInfo().getSize() <=0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
        }
        if (menuItemDtoWithId.getPrice() <=0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s price is not valid");
        }
        if (!MenuService.getInstance().isIdValid(menuItemDtoWithId.getId())) {
            throw new IllegalArgumentException("Error code 400. Menu with such id do not exist");
        }
    }
}
