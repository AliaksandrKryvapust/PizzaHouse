package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public interface IMenuItemService {
    void save(MenuItemDto menuItemDto);
    List<Menu> get();
    IMenu get(Long id);
    Boolean isIdValid(Long id);
    Boolean isDishExist(String name);
    void update(MenuDto menuDto);
    void delete(String id, String version);
}
