package groupId.artifactId.core.dto.input;

public class OrderDtoInput {
    private Long menuItemId;
    private Integer count;

    public OrderDtoInput() {
    }

    public OrderDtoInput(Long menuItemId, Integer count) {
        this.menuItemId = menuItemId;
        this.count = count;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
