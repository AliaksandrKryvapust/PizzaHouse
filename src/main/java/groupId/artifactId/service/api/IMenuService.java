package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;

public interface IMenuService extends IService<MenuDtoOutput, MenuDtoInput>, IServiceUpdate<MenuDtoOutput, MenuDtoInput> {
    MenuDtoOutput getAllData(Long id);

    Boolean isIdValid(Long id);

    Boolean exist(String name);
}
