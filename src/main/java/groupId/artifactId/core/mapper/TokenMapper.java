package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.OrderDto;
import groupId.artifactId.core.dto.SelectedItemDto;
import groupId.artifactId.storage.entity.*;
import groupId.artifactId.storage.entity.api.ISelectedItem;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.ArrayList;
import java.util.List;

public class TokenMapper {
    public static IToken orderMapping(OrderDto orderDto) {
        List<ISelectedItem> temp = new ArrayList<>();
        for (SelectedItemDto select : orderDto.getSelectedItems()) {
            PizzaInfo pizzaInfo = new PizzaInfo(select.getMenuItem().getInfo().getName(),
                    select.getMenuItem().getInfo().getDescription(), select.getMenuItem().getInfo().getSize());
            MenuItem menuItem = new MenuItem(pizzaInfo, select.getMenuItem().getPrice());
            temp.add(new SelectedItem(menuItem, select.getCount()));
        }
        return new Token(new Order(temp));
    }
}
