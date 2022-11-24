package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;

public interface IMenuService extends IService<MenuDtoCrudOutput, MenuDtoInput>, IServiceUpdate<MenuDtoCrudOutput, MenuDtoInput>,
        IServiceDelete {
    MenuDtoOutput getAllData(Long id);
}
