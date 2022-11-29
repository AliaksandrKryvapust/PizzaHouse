package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.util.List;

public class SelectedItemMapper {
    private final MenuItemMapper menuItemMapper;

    public SelectedItemMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    public ISelectedItem inputMapping(SelectedItemDtoInput input, List<IMenuItem> menuItems) {
        return SelectedItem.builder()
                .menuItem(menuItems.stream().filter((i) -> i.getId().equals(input.getMenuItemId())).findFirst().orElse(null))
                .count(input.getCount()).build();
    }

    public SelectedItemDtoOutput outputMapping(ISelectedItem item) {
        MenuItemDtoOutput menuItem = menuItemMapper.outputMapping(item.getMenuItem());
        return SelectedItemDtoOutput.builder()
                .menuItem(menuItem)
                .id(item.getId())
                .count(item.getCount())
                .createdAt(item.getCreateAt())
                .build();

    }
}
