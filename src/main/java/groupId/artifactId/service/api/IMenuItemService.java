package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;

public interface IMenuItemService extends IService<MenuItemDtoOutput, MenuItemDtoInput>,
        IServiceUpdate<MenuItemDtoOutput, MenuItemDtoInput>, IServiceDelete {
}
