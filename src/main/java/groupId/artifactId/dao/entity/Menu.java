package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IMenu {
    private List<MenuItem> items = new ArrayList<>();
    private Long id;
    private LocalDateTime creationDate;
    private Integer version;
    private String name;
    private Boolean enable;

    public Menu() {
    }

    public Menu(List<MenuItem> items) {
        this.items = items;
    }

    public Menu(String name, Boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public Menu(Long id, String name, Boolean enable) {
        this.id = id;
        this.name = name;
        this.enable = enable;
    }

    public Menu(Long id, LocalDateTime creationDate, Integer version, String name, Boolean enable) {
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
        this.name = name;
        this.enable = enable;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    @Override
    public List<MenuItem> getItems() {
        return this.items;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getEnable() {
        return enable;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "items=" + items +
                ", id=" + id +
                ", creationDate=" + creationDate +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", enable=" + enable +
                '}';
    }
}
