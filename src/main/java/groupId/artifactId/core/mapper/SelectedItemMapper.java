package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;

public class SelectedItemMapper {
    private final MenuItemMapper menuItemMapper;

    public SelectedItemMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    public ISelectedItem selectedItemInputMapping(SelectedItemDtoInput input, Long orderId) {
        return new SelectedItem(input.getMenuItemId(), orderId, input.getCount());
    }

    public SelectedItemDtoOutput selectedItemOutputMapping(ISelectedItem item) {
        if (item.getItem() == null) {
            return new SelectedItemDtoOutput(new MenuItemDtoOutput(), item.getId(), item.getMenuItemId(), item.getOrderId(),
                    item.getCount(), item.getCreateAt(), item.getVersion());
        } else {
            MenuItemDtoOutput menuItem = menuItemMapper.menuItemOutputMapping(item.getItem());
            return new SelectedItemDtoOutput(menuItem, item.getId(), item.getMenuItemId(), item.getOrderId(),
                    item.getCount(), item.getCreateAt(), item.getVersion());
        }
    }
}
