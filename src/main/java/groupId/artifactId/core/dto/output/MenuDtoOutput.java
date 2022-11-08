package groupId.artifactId.core.dto.output;

import java.time.Instant;
import java.util.List;

public class MenuDtoOutput {
    private Long id;
    private Instant createdAt;
    private Integer version;
    private String name;
    private Boolean enable;
    private List<MenuItemDtoOutput> items;

    public MenuDtoOutput() {
    }

    public MenuDtoOutput(Long id, Instant createdAt, Integer version, String name, Boolean enable,
                         List<MenuItemDtoOutput> items) {
        this.id = id;
        this.createdAt = createdAt;
        this.version = version;
        this.name = name;
        this.enable = enable;
        this.items = items;
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

    public List<MenuItemDtoOutput> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDtoOutput> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MenuDtoOutput{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", enable=" + enable +
                '}';
    }
}
