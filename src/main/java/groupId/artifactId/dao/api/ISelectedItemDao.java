package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.ISelectedItem;

public interface ISelectedItemDao extends IDao<ISelectedItem>, IDaoDelete {
    ISelectedItem getAllData(Long id);

    Boolean exist(Long id);
}
