package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenu;

public interface IMenuDao extends IDao<IMenu> {

    Boolean exist(Long id);
    Integer getVersion(Long id, Integer version);

    Boolean doesMenuExist(String name);
}

