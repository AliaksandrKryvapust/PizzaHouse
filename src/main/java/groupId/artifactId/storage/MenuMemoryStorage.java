package groupId.artifactId.storage;

import groupId.artifactId.storage.entity.Menu;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.api.IMenuStorage;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MenuMemoryStorage implements IMenuStorage {

    private final List<IMenu> menuList = new ArrayList<>();

    public MenuMemoryStorage() {
    }

    @Override
    public List<IMenu> get() {
        return this.menuList;
    }

    @Override
    public void add(IMenu menu) {
        if (menu.getId() != null) {
            throw new IllegalStateException("Error code 500. Menu id should be empty");
        }
        Menu temp = (Menu) menu;
        temp.setId(menuList.size() + 1);
        this.menuList.add(temp);
    }

    @Override
    public void saveNew(IMenu menu) {
        this.menuList.clear();
        this.menuList.add(menu);
    }

    @Override
    public void addMenuItem(IMenuItem menuItem, int menuId) {
        IMenu menu = this.getById(menuId).orElse(null);
        if (menu == null) {
            throw new IllegalStateException("There is no such menu id in the storage");
        } else {
            menu.getItems().add(menuItem);
            this.add(menu);
        }
    }

    @Override
    public Optional<IMenu> getById(int id) {
        return this.menuList.stream().filter((i) -> i.getId() == id).findFirst();
    }
}
