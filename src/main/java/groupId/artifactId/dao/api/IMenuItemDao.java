package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenuItem;

public interface IMenuItemDao extends IDao<IMenuItem>, IDaoUpdate<IMenuItem> {
    IMenuItem getAllData(Long id);
    Boolean exist(Long id);
}
