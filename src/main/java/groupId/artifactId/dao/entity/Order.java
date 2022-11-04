package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.time.LocalDateTime;
import java.util.List;

public class Order implements IOrder {
    private List<ISelectedItem> selectedItems;
    private Long id;
    private LocalDateTime creationDate;
    private Integer version;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(List<ISelectedItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Order(Long id, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    public Order(List<ISelectedItem> selectedItems, Long id, LocalDateTime creationDate, Integer version) {
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
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
