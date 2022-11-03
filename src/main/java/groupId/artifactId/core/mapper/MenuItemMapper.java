package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;

public class MenuItemMapper {
    public static IMenuItem menuItemInputMapping(MenuItemDtoInput menuItemDtoInput) {
        return new MenuItem(menuItemDtoInput.getPrice(), menuItemDtoInput.getPizzaInfoId(), menuItemDtoInput.getMenuId());
    }

    public static MenuItemDtoOutput menuItemOutputMapping(IMenuItem menuItem) {
        return new MenuItemDtoOutput(menuItem.getId(), menuItem.getPrice(), menuItem.getPizzaInfoId(), menuItem.getCreationDate(),
                menuItem.getVersion(), menuItem.getMenuId());
    }
}
