package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public interface IMenuService extends IService<IMenu, MenuDto>{
    void addMenuItem(MenuItemDto menuItemDto);
    Boolean isIdValid(Long id);
    Boolean isDishExist(String name);
}
