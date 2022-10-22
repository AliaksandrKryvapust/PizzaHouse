package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public interface IMenuService {
    void add(List<MenuItemDto> menuItemDto);
    void addMenuItem(MenuItemDtoWithId menuItemDtoWithId);
    List<Menu> get();
    IMenu get(Long id);
    Boolean isIdValid(Long id);
    Boolean isDishExist(String name);
}
