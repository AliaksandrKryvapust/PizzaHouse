package groupId.artifactId.core.mapper;

import groupId.artifactId.entity.api.IMenuItem;
import groupId.artifactId.storage.entity.Menu;
import groupId.artifactId.storage.entity.api.IMenu;

import java.util.List;

public class MenuMapper {
    public static IMenu menuMapping(List<IMenuItem> menuItem) {
        return new Menu(menuItem);
    }
}
