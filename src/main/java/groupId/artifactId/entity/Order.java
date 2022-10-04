package groupId.artifactId.entity;

import groupId.artifactId.entity.api.IOrder;
import groupId.artifactId.entity.api.ISelectedItem;

import java.util.List;

public class Order implements IOrder {
    private List<ISelectedItem> selectedItems;

    public Order(List<ISelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void setSelectedItems(List<ISelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public List<ISelectedItem> getSelectedItems() {
        return selectedItems;
    }
}
