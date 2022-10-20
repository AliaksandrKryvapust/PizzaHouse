package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenu;

import java.util.ArrayList;
import java.util.List;

public class Menu implements IMenu {
    private List<MenuItem> items = new ArrayList<>();
    private Long id;

    public Menu() {
    }

    public Menu(List<MenuItem> items, Long id) {
        this.items = items;
        this.id = id;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public List<MenuItem> getItems() {
        return this.items;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
