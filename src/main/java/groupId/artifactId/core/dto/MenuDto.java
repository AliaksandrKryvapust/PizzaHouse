package groupId.artifactId.core.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuDto {

    private List<MenuItemDto> items = new ArrayList<>();
    private Long id;
    private Integer version;

    public MenuDto() {
    }

    public MenuDto(List<MenuItemDto> items) {
        this.items = items;
    }

    public List<MenuItemDto> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDto> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
