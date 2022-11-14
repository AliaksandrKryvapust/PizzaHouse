package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.time.Instant;
import java.util.List;

public class Order implements IOrder {
    private List<ISelectedItem> selectedItems;
    private Long id;
    private Instant creationDate;
    private Integer version;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(List<ISelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Order(List<ISelectedItem> selectedItems, Long id) {
        this.selectedItems = selectedItems;
        this.id = id;
    }

    public Order(Long id, Instant creationDate, Integer version) {
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    public Order(List<ISelectedItem> selectedItems, Long id, Instant creationDate, Integer version) {
        this.selectedItems = selectedItems;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    @Override
    public List<ISelectedItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<ISelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Order{" +
                "selectedItems=" + selectedItems +
                ", id=" + id +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
