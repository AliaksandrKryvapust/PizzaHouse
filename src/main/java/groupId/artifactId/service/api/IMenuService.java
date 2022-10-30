package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.core.dto.output.MenuDtoOutput;

public interface IMenuService extends IService<MenuDtoOutput, MenuDtoInput> {
    void addMenuItem(MenuItemDto menuItemDto);

    Boolean isIdValid(Long id);

    Boolean isDishExist(String name);
}
