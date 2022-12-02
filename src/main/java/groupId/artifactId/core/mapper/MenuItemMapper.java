package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class MenuItemMapper {
    private final PizzaInfoMapper pizzaInfoMapper;

    public MenuItemMapper(PizzaInfoMapper pizzaInfoMapper) {
        this.pizzaInfoMapper = pizzaInfoMapper;
    }

    public IMenuItem inputMapping(MenuItemDtoInput menuItemDtoInput) {
        IPizzaInfo pizzaInfo = pizzaInfoMapper.inputMapping(menuItemDtoInput.getPizzaInfoDtoInput());
        return MenuItem.builder().price(menuItemDtoInput.getPrice())
                .pizzaInfo((PizzaInfo) pizzaInfo).build();
    }

    public MenuItemDtoOutput outputMapping(IMenuItem menuItem) {
        PizzaInfoDtoOutput pizzaInfo = pizzaInfoMapper.outputMapping(menuItem.getPizzaInfo());
        return MenuItemDtoOutput.builder()
                .id(menuItem.getId())
                .price(menuItem.getPrice())
                .createdAt(menuItem.getCreationDate())
                .version(menuItem.getVersion())
                .pizzaInfo(pizzaInfo).build();
    }
}
