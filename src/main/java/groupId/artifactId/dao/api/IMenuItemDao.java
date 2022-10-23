package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenuItem;

import java.util.List;

public interface IMenuItemDao extends IDao<IMenuItem> {
    List<MenuItem> get();

    Boolean isIdExist(Long id);
}
