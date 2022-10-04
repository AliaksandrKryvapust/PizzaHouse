package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.storage.entity.MenuItem;
import groupId.artifactId.storage.entity.api.IMenuItem;
import groupId.artifactId.storage.entity.Menu;
import groupId.artifactId.storage.entity.api.IMenu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static IMenu menuMapping(List<MenuItemDto> menuItemDto) {
        List<IMenuItem> temp = menuItemDto.stream().map(
                (i)-> new MenuItem(i.getInfo(),i.getPrice())).collect(Collectors.toList());
        return new Menu(temp);
    }
    public static IMenuItem menuItemMapping(MenuItemDto menuItemDto){
        return new MenuItem(menuItemDto.getInfo(),menuItemDto.getPrice());
    }
    public static IMenuItem menuItemWithIdMapping(MenuItemDtoWithId menuItemDtoWithId){
        return new MenuItem(menuItemDtoWithId.getInfo(), menuItemDtoWithId.getPrice());
    }
}
