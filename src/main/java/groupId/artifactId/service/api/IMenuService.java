package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;

import javax.persistence.EntityManager;

public interface IMenuService extends IService<MenuDtoCrudOutput, MenuDtoInput>, IServiceUpdate<MenuDtoCrudOutput, MenuDtoInput>,
        IServiceDelete {
    MenuDtoOutput getAllData(Long id);

    IMenu getRow(Long id, EntityManager entityTransaction);

    IMenu updateItem(IMenu menu, IMenuItem menuItem, EntityManager entityTransaction);
}
