package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;

import java.util.List;

public interface IMenuValidator {
    void validateListMenuItems(List<MenuItemDto> menuItemDto);
    void validateMenuItem(MenuItemDtoWithId menuItemDtoWithId);
    void validateMenu(MenuDto menuDto);
}
