package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;

import java.util.List;

public interface IMenuItemService {
    void save(MenuItemDto menuItemDto);
    List<IMenuItem> get();
    IMenuItem get(Long id);
    Boolean isIdValid(Long id);
    void update(MenuItemDto menuItemDto);
    void delete(String id, String version);
}
