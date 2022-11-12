package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuItemDtoCrudOutput;

public interface IMenuItemService extends IService<MenuItemDtoCrudOutput, MenuItemDtoInput>,
        IServiceUpdate<MenuItemDtoCrudOutput, MenuItemDtoInput>, IServiceDelete {
    MenuItemDtoOutput getAllData(Long id);

    Boolean isIdValid(Long id);
}
