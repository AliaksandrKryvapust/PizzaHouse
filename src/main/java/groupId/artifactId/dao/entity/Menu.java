package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IMenu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu implements IMenu {
    private List<MenuItem> items = new ArrayList<>();
    private Long id;
    private LocalDateTime creationDate;
    private LocalDateTime editDate;

    public Menu() {
    }

    public Menu(List<MenuItem> items) {
        this.items = items;
    }

    public Menu(List<MenuItem> items, Long id) {
        this.items = items;
        this.id = id;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
    }

    @Override
    public List<MenuItem> getItems() {
        return this.items;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "items=" + items +
                ", id=" + id +
                ", creationDate=" + creationDate +
                ", editDate=" + editDate +
                '}';
    }
}
