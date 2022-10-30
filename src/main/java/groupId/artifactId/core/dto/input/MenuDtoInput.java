package groupId.artifactId.core.dto.input;

import java.util.ArrayList;
import java.util.List;

public class MenuDtoInput {

    private List<MenuItemDto> items = new ArrayList<>();
    private Long id;
    private Integer version;
    private String name;
    private Boolean enable;

    public MenuDtoInput() {
    }

    public MenuDtoInput(List<MenuItemDto> items) {
        this.items = items;
    }

    public MenuDtoInput(List<MenuItemDto> items, Long id, String name, Boolean enable) {
        this.items = items;
        this.id = id;
        this.name = name;
        this.enable = enable;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
