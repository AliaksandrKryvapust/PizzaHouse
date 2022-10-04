package groupId.artifactId.storage.entity;

import groupId.artifactId.storage.entity.api.IMenu;
import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.List;

public class Menu implements IMenu {
    private final List<IMenuItem> items;

    public Menu(List<IMenuItem> items) {
        this.items = items;
    }

    @Override
    public List<IMenuItem> getItems() {
        return this.items;
    }
}
