package groupId.artifactId.core.dto.input;

import java.util.List;

public class OrderDtoInput {
    List<SelectedItemDtoInput> selectedItems;

    public OrderDtoInput() {
    }

    public OrderDtoInput(List<SelectedItemDtoInput> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<SelectedItemDtoInput> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItemDtoInput> selectedItems) {
        this.selectedItems = selectedItems;
    }

    @Override
    public String toString() {
        return "OrderDtoInput{" +
                "selectedItems=" + selectedItems +
                '}';
    }
}
