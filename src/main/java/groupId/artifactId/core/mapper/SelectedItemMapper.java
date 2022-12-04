package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.entity.SelectedItem;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SelectedItemMapper {
    private final MenuItemMapper menuItemMapper;
    @Autowired
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
