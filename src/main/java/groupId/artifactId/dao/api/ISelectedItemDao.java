package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.ISelectedItem;

public interface ISelectedItemDao extends IDao<ISelectedItem> {
    ISelectedItem getAllData(Long id);

    Boolean exist(Long id);
}
