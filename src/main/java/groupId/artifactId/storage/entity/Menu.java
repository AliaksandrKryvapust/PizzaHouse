package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.List;

public class Menu implements IMenu {
    private final List<IMenuItem> items;
    private Integer id;

    public Menu(List<IMenuItem> items) {
        this.items = items;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public List<IMenuItem> getItems() {
        return this.items;
    }
}
