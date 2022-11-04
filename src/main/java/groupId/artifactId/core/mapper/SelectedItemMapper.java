package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;

public class SelectedItemMapper {
    public static ISelectedItem selectedItemInputMapping(SelectedItemDtoInput input, Long orderId) {
        return new SelectedItem(input.getMenuItemId(), orderId, input.getCount());
    }

    public static SelectedItemDtoOutput selectedItemOutputMapping(ISelectedItem item) {
        MenuItemDtoOutput menuItem = MenuItemMapper.menuItemOutputMapping(item.getItem());
        return new SelectedItemDtoOutput(menuItem, item.getId(), item.getMenuItemId(), item.getOrderId(),
                item.getCount(), item.getCreateAt(), item.getVersion());
    }
}
