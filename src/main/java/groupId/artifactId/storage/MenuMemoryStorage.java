package groupId.artifactId.storage;

import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.api.IMenuStorage;

import java.util.ArrayList;
import java.util.List;


public class MenuMemoryStorage implements IMenuStorage {

    private final List<IMenu> menuList = new ArrayList<>();

    public MenuMemoryStorage() {
    }

    @Override
    public List<IMenu> get() {
        return this.menuList;
    }

    @Override
    public void save(IMenu menu) {
        this.menuList.add(menu);
    }
}
