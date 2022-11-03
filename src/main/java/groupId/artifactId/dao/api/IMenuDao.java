package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenu;

public interface IMenuDao extends IDao<IMenu> {
    IMenu getAllData(Long id);

    Boolean exist(Long id);

    Boolean doesMenuExist(String name);
}

