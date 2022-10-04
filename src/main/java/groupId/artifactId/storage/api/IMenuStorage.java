package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.Optional;

public interface IMenuStorage extends IEssenceStorage<IMenu>{
    void saveNew (IMenu menu);
    void addMenuItem(IMenuItem menuItem, int menuId);
    Optional<IMenu> getById(int id);
}
