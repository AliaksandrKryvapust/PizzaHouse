package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.dao.entity.api.IMenu;

public interface IMenuService extends IService<IMenu, MenuDtoInput>{
    void addMenuItem(MenuItemDto menuItemDto);
    Boolean isIdValid(Long id);
    Boolean isDishExist(String name);
}
