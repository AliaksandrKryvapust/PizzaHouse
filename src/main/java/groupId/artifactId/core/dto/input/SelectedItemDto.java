package groupId.artifactId.core.dto.input;

public class SelectedItemDto {
    private MenuItemDto menuItem;
    private Integer count;

    public SelectedItemDto() {
    }

    public SelectedItemDto(MenuItemDto menuItem, Integer count) {
        this.menuItem = menuItem;
        this.count = count;
    }

    public MenuItemDto getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemDto menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
