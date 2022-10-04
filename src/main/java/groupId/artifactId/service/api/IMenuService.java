package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.List;

public interface IMenuService {
    void add(List<MenuItemDto> menuItemDto);
    void addMenuItem(MenuItemDtoWithId menuItemDtoWithId);
    List<IMenu> get();
    Boolean isIdValid(int id);
}
