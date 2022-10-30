package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;

import java.util.List;

public interface IMenuValidator {
    void validateListMenuItems(List<MenuItemDto> menuItemDto);
    void validateMenuItem(MenuItemDto menuItemDto);
    void validateMenu(MenuDtoInput menuDtoInput);
    void validateMenuRow(MenuDtoInput menuDtoInput);
}
