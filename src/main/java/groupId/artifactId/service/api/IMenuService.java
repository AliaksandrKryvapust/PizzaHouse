package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.storage.entity.api.IMenu;

import java.util.List;

public interface IMenuService extends IEssenceService{
    void add(List<MenuItemDto> menuItemDto);
}
