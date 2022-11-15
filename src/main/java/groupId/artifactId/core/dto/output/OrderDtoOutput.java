package groupId.artifactId.core.dto.output;

import java.time.Instant;
import java.util.List;

public class OrderDtoOutput {
    private List<SelectedItemDtoOutput> selectedItems;
    private Long id;
    private Instant createdAt;
    private Integer version;

    public OrderDtoOutput() {
    }

    public OrderDtoOutput(List<SelectedItemDtoOutput> selectedItems, Long id, Instant createdAt, Integer version) {
        this.selectedItems = selectedItems;
        this.id = id;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
