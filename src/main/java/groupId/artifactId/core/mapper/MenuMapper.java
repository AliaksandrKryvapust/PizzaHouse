package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuMapper {
    private final MenuItemMapper menuItemMapper;

    public MenuMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    public IMenu menuInputMapping(MenuDtoInput menuDtoInput) {
        return new Menu(menuDtoInput.getName(), menuDtoInput.getEnable());
    }

    public MenuDtoOutput menuOutputMapping(IMenu menu) {
        List<MenuItemDtoOutput> items = new ArrayList<>();
        for (IMenuItem menuItem : menu.getItems()) {
            MenuItemDtoOutput item = menuItemMapper.menuItemOutputMapping(menuItem);
            items.add(item);
        }
        return new MenuDtoOutput(menu.getId(), menu.getCreationDate(), menu.getVersion(), menu.getName(), menu.getEnable(),
                items);
    }
}
