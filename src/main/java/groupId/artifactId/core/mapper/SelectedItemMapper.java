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

    public ISelectedItem inputMapping(SelectedItemDtoInput input, Long orderId) {
        return new SelectedItem(input.getMenuItemId(), orderId, input.getCount());
    }

    public SelectedItemDtoOutput outputMapping(ISelectedItem item) {
        MenuItemDtoOutput menuItem = menuItemMapper.outputMapping(item.getItem());
        return SelectedItemDtoOutput.builder()
                .menuItem(menuItem)
                .id(item.getId())
                .menuItemId(item.getMenuItemId())
                .orderId(item.getOrderId())
                .count(item.getCount())
                .createdAt(item.getCreateAt())
                .version(item.getVersion()).build();

    }
}
