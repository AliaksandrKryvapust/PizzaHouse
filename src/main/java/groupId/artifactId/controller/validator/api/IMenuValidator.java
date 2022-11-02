package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;

import java.util.List;

public interface IMenuValidator extends IValidator<MenuDtoInput> {
    void validateListMenuItems(List<MenuItemDto> menuItemDto);
    void validateMenuItem(MenuItemDto menuItemDto);
}
