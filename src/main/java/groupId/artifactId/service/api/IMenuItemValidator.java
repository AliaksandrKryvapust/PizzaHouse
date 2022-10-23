package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;

import java.util.List;

public interface IMenuItemValidator {
    void validateListMenuItems(List<MenuItemDto> menuItemDto);
    void validateMenuItem(MenuItemDto menuItemDto);
}
