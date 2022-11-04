package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.ISelectedItem;

import java.time.LocalDateTime;

public class SelectedItem implements ISelectedItem {
    private IMenuItem menuItem;
    private Long id;
    private Long menuItemId;
    private Long orderId;
    private Integer count;
    private LocalDateTime createAt;
    private Integer version;

    public SelectedItem() {
    }

    public SelectedItem(Long menuItemId, Long orderId, Integer count) {
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
    }

    public SelectedItem(Long id, Long menuItemId, Long orderId, Integer count) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
    }

    public SelectedItem(Long id, Long menuItemId, Long orderId, Integer count, LocalDateTime createAt, Integer version) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
        this.createAt = createAt;
        this.version = version;
    }

    public SelectedItem(IMenuItem menuItem, Long id, Long menuItemId, Long orderId, Integer count,
                        LocalDateTime createAt, Integer version) {
        this.menuItem = menuItem;
        this.id = id;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
        this.createAt = createAt;
        this.version = version;
    }

    @Override
    public IMenuItem getItem() {
        return menuItem;
    }

    public void setMenuItem(IMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
