package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class SelectedItemDtoCrudOutput {
    private Long id;
    private Long menuItemId;
    private Long orderId;
    private Integer count;
    private Instant createAt;
    private Integer version;

    public SelectedItemDtoCrudOutput() {
    }

    public SelectedItemDtoCrudOutput(Long id, Long menuItemId, Long orderId, Integer count,
                                     Instant createAt, Integer version) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
        this.count = count;
        this.createAt = createAt;
        this.version = version;
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

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
