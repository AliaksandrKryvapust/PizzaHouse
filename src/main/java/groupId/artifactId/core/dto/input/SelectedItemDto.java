package groupId.artifactId.core.dto.input;

public class SelectedItemDto {
    private MenuItemDtoInput menuItem;
    private Integer count;

    public SelectedItemDto() {
    }

    public SelectedItemDto(MenuItemDtoInput menuItem, Integer count) {
        this.menuItem = menuItem;
        this.count = count;
    }

    public MenuItemDtoInput getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItemDtoInput menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
