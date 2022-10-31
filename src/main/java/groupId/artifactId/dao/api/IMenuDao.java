package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;

public interface IMenuDao extends IDao<IMenu> {

    void add(MenuItem menuItem, Long menuId);

    Boolean exist(Long id);

    Boolean doesMenuExist(String name);
}

