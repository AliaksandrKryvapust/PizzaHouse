package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
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

    public IMenu inputMapping(MenuDtoInput menuDtoInput) {
        return Menu.builder()
                .name(menuDtoInput.getName())
                .enable(menuDtoInput.getEnable()).build();
    }

    public MenuDtoCrudOutput outputCrudMapping(IMenu menu) {
        return MenuDtoCrudOutput.builder()
                .id(menu.getId())
                .createdAt(menu.getCreationDate())
                .version(menu.getVersion())
                .name(menu.getName())
                .enable(menu.getEnable()).build();
    }

    public MenuDtoOutput outputMapping(IMenu menu) {
        List<MenuItemDtoOutput> items = new ArrayList<>();
        for (IMenuItem menuItem : menu.getItems()) {
            MenuItemDtoOutput item = menuItemMapper.outputMapping(menuItem);
            items.add(item);
        }
        return MenuDtoOutput.builder()
                .id(menu.getId())
                .createdAt(menu.getCreationDate())
                .version(menu.getVersion())
                .name(menu.getName())
                .enable(menu.getEnable())
                .items(items).build();
    }
}
