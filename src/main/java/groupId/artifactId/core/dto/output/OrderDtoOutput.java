package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDtoOutput {
    private List<SelectedItemDtoOutput> selectedItems;
    private Long id;
    private LocalDateTime creationDate;
    private Integer version;

    public OrderDtoOutput() {
    }

    public OrderDtoOutput(List<SelectedItemDtoOutput> selectedItems, Long id, LocalDateTime creationDate, Integer version) {
        this.selectedItems = selectedItems;
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    public List<SelectedItemDtoOutput> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<SelectedItemDtoOutput> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
