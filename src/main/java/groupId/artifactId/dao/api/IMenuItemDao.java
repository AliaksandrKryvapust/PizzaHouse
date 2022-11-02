package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenuItem;

public interface IMenuItemDao extends IDao<IMenuItem> {
    Boolean exist(Long id);
}
