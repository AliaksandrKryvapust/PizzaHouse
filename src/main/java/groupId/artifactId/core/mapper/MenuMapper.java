package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDto;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static IMenu menuInputMapping(MenuDtoInput menuDtoInput) {
        return new Menu(menuDtoInput.getName(), menuDtoInput.getEnable());
    }

    public static MenuDtoOutput menuOutputMapping(IMenu menu) {
        return new MenuDtoOutput(menu.getId(), menu.getCreationDate(), menu.getVersion(), menu.getName(), menu.getEnable());
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
