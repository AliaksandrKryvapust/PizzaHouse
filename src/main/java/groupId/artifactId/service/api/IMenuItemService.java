package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.dao.entity.api.IMenuItem;

import javax.persistence.EntityManager;
import java.util.List;

public interface IMenuItemService extends IService<MenuItemDtoOutput, MenuItemDtoInput>,
        IServiceUpdate<MenuItemDtoOutput, MenuItemDtoInput>, IServiceDelete {
    List<IMenuItem> getRow(List<Long> ids, EntityManager entityTransaction);
}
