package groupId.artifactId.entity;

import groupId.artifactId.storage.entity.api.IMenuItem;
import groupId.artifactId.entity.api.ISelectedItem;

public class SelectedItem implements ISelectedItem {
    private IMenuItem menuItem;
    private Integer count;

    public SelectedItem(IMenuItem menuItem, Integer count) {
        this.menuItem = menuItem;
        this.count = count;
    }

    public void setMenuItem(IMenuItem menuItem) {
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
