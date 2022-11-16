package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;

public class MenuItemMapper {
    private final PizzaInfoMapper pizzaInfoMapper;

    public MenuItemMapper(PizzaInfoMapper pizzaInfoMapper) {
        this.pizzaInfoMapper = pizzaInfoMapper;
    }

    public IMenuItem inputMapping(MenuItemDtoInput menuItemDtoInput) {
        return new MenuItem(menuItemDtoInput.getPrice(), menuItemDtoInput.getPizzaInfoId(), menuItemDtoInput.getMenuId());
    }

    public MenuItemDtoCrudOutput outputCrudMapping(IMenuItem menuItem) {
        return MenuItemDtoCrudOutput.builder()
                .id(menuItem.getId())
                .price(menuItem.getPrice())
                .pizzaInfoId(menuItem.getPizzaInfoId())
                .createdAt(menuItem.getCreationDate())
                .version(menuItem.getVersion())
                .menuId(menuItem.getMenuId()).build();
    }

    public MenuItemDtoOutput outputMapping(IMenuItem menuItem) {
        PizzaInfoDtoOutput pizzaInfo = pizzaInfoMapper.outputMapping(menuItem.getInfo());
        return MenuItemDtoOutput.builder()
                .id(menuItem.getId())
                .price(menuItem.getPrice())
                .pizzaInfoId(menuItem.getPizzaInfoId())
                .createdAt(menuItem.getCreationDate())
                .version(menuItem.getVersion())
                .menuId(menuItem.getMenuId())
                .pizzaInfo(pizzaInfo).build();
    }
}
