package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;

import java.util.List;

public interface IMenuValidator {
    void validate(List<MenuItemDto> menuItemDto);
}
