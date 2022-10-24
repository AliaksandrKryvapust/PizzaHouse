package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.MenuDto;
import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.Menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static Menu menuMapping(MenuDto menuDto) {
        Menu menu =  new Menu();
        menu.setId(menuDto.getId());
        menu.setVersion(menuDto.getVersion());
        List<MenuItem> temp = menuDto.getItems().stream().map(
                (i) -> new MenuItem(new PizzaInfo(i.getInfo().getName(), i.getInfo().getDescription(),
                        i.getInfo().getSize()), i.getPrice())).collect(Collectors.toList());
        menu.setItems(temp);
        return menu;
    }
    public static Menu menuItemsMapping(List<MenuItemDto> menuItemDto) {
        List<MenuItem> temp = menuItemDto.stream().map(
                (i) -> new MenuItem(new PizzaInfo(i.getInfo().getName(), i.getInfo().getDescription(),
                        i.getInfo().getSize()), i.getPrice())).collect(Collectors.toList());
        return new Menu(temp);
    }

    public static MenuItem menuItemMapping(MenuItemDto menuItemDto) {
        return new MenuItem(menuItemDto.getId(), new PizzaInfo(menuItemDto.getInfo().getName(), menuItemDto.getInfo().getDescription(),
                menuItemDto.getInfo().getSize()), menuItemDto.getPrice(),  menuItemDto.getVersion());
    }
}
