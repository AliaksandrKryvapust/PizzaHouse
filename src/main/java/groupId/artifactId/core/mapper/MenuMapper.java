package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.api.IMenu;

public class MenuMapper {
    public static IMenu menuInputMapping(MenuDtoInput menuDtoInput) {
        return new Menu(menuDtoInput.getName(), menuDtoInput.getEnable());
    }

    public static MenuDtoOutput menuOutputMapping(IMenu menu) {
        return new MenuDtoOutput(menu.getId(), menu.getCreationDate(), menu.getVersion(), menu.getName(), menu.getEnable());
    }
}
