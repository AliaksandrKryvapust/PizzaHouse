package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static Menu menuMapping(List<MenuItemDto> menuItemDto) {
        List<MenuItem> temp = menuItemDto.stream().map(
                (i) -> new MenuItem(new PizzaInfo(i.getInfo().getName(), i.getInfo().getDescription(),
                        i.getInfo().getSize()), i.getPrice())).collect(Collectors.toList());
        return new Menu(temp);
    }

    public static groupId.artifactId.dao.entity.MenuItem menuItemWithIdMapping(MenuItemDtoWithId menuItemDtoWithId) {
        return new groupId.artifactId.dao.entity.MenuItem(new groupId.artifactId.dao.entity.PizzaInfo(menuItemDtoWithId.getInfo().getName(), menuItemDtoWithId.getInfo().getDescription(),
                menuItemDtoWithId.getInfo().getSize()), menuItemDtoWithId.getPrice());
    }
}
