package groupId.artifactId.service;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.storage.entity.api.IMenu;

import java.util.List;

public class OrderDataService implements IOrderDataService {
    @Override
    public void add(List<MenuItemDto> menuItemDto) {

    }

    @Override
    public void addMenuItem(MenuItemDtoWithId menuItemDtoWithId) {

    }

    @Override
    public List<IMenu> get() {
        return null;
    }

    @Override
    public Boolean isIdValid(int id) {
        return null;
    }

    @Override
    public Boolean isDishExist(String name) {
        return null;
    }
}
