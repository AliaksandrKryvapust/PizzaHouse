package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.List;

public class Menu implements IMenu {
    private final List<IMenuItem> items;
    private Long id;

    public Menu(List<IMenuItem> items) {
        this.items = items;
    }

    @Override
    public List<IMenuItem> getItems() {
        return this.items;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
