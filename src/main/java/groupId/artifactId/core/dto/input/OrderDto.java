package groupId.artifactId.core.dto.input;

import java.util.List;

public class OrderDto {
    private List<SelectedItemDto> selectedItems;

    public OrderDto() {
    }

    public OrderDto(List<SelectedItemDto> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<SelectedItemDto> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItemDto> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
