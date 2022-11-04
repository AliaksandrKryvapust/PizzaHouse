package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;

public class SelectedItemDtoOutput {
    private MenuItemDtoOutput menuItem;
    private Long id;
    private Long menuItemId;
    private Long orderId;
    private Integer count;
    private LocalDateTime createAt;
    private Integer version;

    public SelectedItemDtoOutput() {
    }

    public SelectedItemDtoOutput(MenuItemDtoOutput menuItem, Long id, Long menuItemId, Long orderId, Integer count,
                                 LocalDateTime createAt, Integer version) {
        this.menuItem = menuItem;
        this.id = id;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
        this.createAt = createAt;
        this.version = version;
    }

    public MenuItemDtoOutput getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemDtoOutput menuItem) {
        this.menuItem = menuItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}