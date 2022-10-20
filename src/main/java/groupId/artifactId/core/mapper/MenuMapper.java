package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.MenuItemDto;
import groupId.artifactId.core.dto.MenuItemDtoWithId;
import groupId.artifactId.exceptions.IncorrectSQLConnectionException;
import groupId.artifactId.storage.entity.MenuItem;
import groupId.artifactId.storage.entity.PizzaInfo;
import groupId.artifactId.storage.entity.api.IMenuItem;
import groupId.artifactId.storage.entity.Menu;
import groupId.artifactId.storage.entity.api.IMenu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static IMenu menuMapping(List<MenuItemDto> menuItemDto) {
        List<IMenuItem> temp = menuItemDto.stream().map(
                (i) -> new MenuItem(new PizzaInfo(i.getInfo().getName(), i.getInfo().getDescription(),
                        i.getInfo().getSize()), i.getPrice())).collect(Collectors.toList());
        return new Menu(temp);
    }
    public static groupId.artifactId.dao.entity.Menu menuMapping(ResultSet resultSet) {
        try {
            groupId.artifactId.dao.entity.PizzaInfo pizzaInfo = new groupId.artifactId.dao.entity.PizzaInfo();
            pizzaInfo.setId(resultSet.getLong("pizza_info_id"));
            pizzaInfo.setName(resultSet.getString("name"));
            pizzaInfo.setDescription(resultSet.getString("description"));
            long size = resultSet.getLong("size");
            if (!resultSet.wasNull()){
                pizzaInfo.setSize(size);
            }
            List<groupId.artifactId.dao.entity.MenuItem> list = new ArrayList<>();
            groupId.artifactId.dao.entity.MenuItem menuItem = new groupId.artifactId.dao.entity.MenuItem();
            menuItem.setId(resultSet.getLong("menu_item_id"));
            menuItem.setPizzaInfo(pizzaInfo);
            double price =resultSet.getDouble("price");
            if (!resultSet.wasNull()){
                menuItem.setPrice(price);
            }
            list.add(menuItem);
            groupId.artifactId.dao.entity.Menu menu = new groupId.artifactId.dao.entity.Menu();
            menu.setId(resultSet.getLong("id"));
            menu.setItems(list);
            return menu;
        } catch (SQLException e) {
            throw new IncorrectSQLConnectionException("Error while mapping IMenu from DB",e);
        }
    }

    public static IMenuItem menuItemWithIdMapping(MenuItemDtoWithId menuItemDtoWithId) {
        return new MenuItem(new PizzaInfo(menuItemDtoWithId.getInfo().getName(), menuItemDtoWithId.getInfo().getDescription(),
                menuItemDtoWithId.getInfo().getSize()), menuItemDtoWithId.getPrice());
    }
}
