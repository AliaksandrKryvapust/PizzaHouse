package groupId.artifactId.storage.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.storage.entity.api.ISelectedItem;

public class SelectedItem implements ISelectedItem {
    private groupId.artifactId.dao.entity.api.IMenuItem menuItem;
    private Integer count;

    public SelectedItem(groupId.artifactId.dao.entity.api.IMenuItem menuItem, Integer count) {
        this.menuItem = menuItem;
        this.count = count;
    }

    public void setMenuItem(groupId.artifactId.dao.entity.api.IMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public IMenuItem getItem() {
        return menuItem;
    }

    @Override
    public int getCount() {
        return count;
    }
}
