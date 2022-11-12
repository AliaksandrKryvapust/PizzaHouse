package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;

public class MenuItemMapper {
    private final PizzaInfoMapper pizzaInfoMapper;

    public MenuItemMapper(PizzaInfoMapper pizzaInfoMapper) {
        this.pizzaInfoMapper = pizzaInfoMapper;
    }

    public IMenuItem menuItemInputMapping(MenuItemDtoInput menuItemDtoInput) {
        return new MenuItem(menuItemDtoInput.getPrice(), menuItemDtoInput.getPizzaInfoId(), menuItemDtoInput.getMenuId());
    }

    public MenuItemDtoOutput menuItemOutputMapping(IMenuItem menuItem) {
        if (menuItem.getInfo()==null){
            return new MenuItemDtoOutput(menuItem.getId(), menuItem.getPrice(), menuItem.getPizzaInfoId(), menuItem.getCreationDate(),
                    menuItem.getVersion(), menuItem.getMenuId(), new PizzaInfoDtoOutput());
        } else {
            PizzaInfoDtoOutput pizzaInfo = pizzaInfoMapper.pizzaInfoOutputMapping(menuItem.getInfo());
            return new MenuItemDtoOutput(menuItem.getId(), menuItem.getPrice(), menuItem.getPizzaInfoId(), menuItem.getCreationDate(),
                    menuItem.getVersion(), menuItem.getMenuId(), pizzaInfo);
        }
    }
}
