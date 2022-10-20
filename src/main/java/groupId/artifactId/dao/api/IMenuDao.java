package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.dao.entity.api.IMenuItem;

import java.util.List;

public interface IMenuDao extends IDao<IMenu> {
    List<IMenu> get();
    void update(IMenuItem menuItem, int menuId);
    Boolean isIdExist(int id);
    Boolean isDishExist(String name);
}
