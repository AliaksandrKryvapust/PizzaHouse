package groupId.artifactId.core.dto.input;

public class SelectedItemDtoInput {
    private Long menuItemId;
    private Integer count;

    public SelectedItemDtoInput() {
    }

    public SelectedItemDtoInput(Long menuItemId, Integer count) {
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
