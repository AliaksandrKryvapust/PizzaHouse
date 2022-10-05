package groupId.artifactId.storage;

import groupId.artifactId.storage.entity.Menu;
import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.api.IMenuStorage;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Menu old = (Menu) this.menuList.get(menu.getId()-1);
        Menu temp = (Menu) menu;
        temp.setId(old.getId());
        this.menuList.set(old.getId()-1,temp);
    }

    @Override
    public void addMenuItem(IMenuItem menuItem, int menuId) {
    Objects.requireNonNull(this.getById(menuId).orElse(null)).getItems().add(menuItem);
    }

    @Override
    public Optional<IMenu> getById(int id) {
        return this.menuList.stream().filter((i) -> i.getId() == id).findFirst();
    }

    @Override
    public Boolean isIdExist(int id) {
        return this.menuList.stream().anyMatch((i)->i.getId()==id);
    }

    @Override
    public Boolean isDishExist(String name) {
        boolean temp=false;
        for (IMenu menu: menuList ) {
           temp= menu.getItems().stream().anyMatch((i)->i.getInfo().getName().equals(name));
        }

        return temp;
    }
}
