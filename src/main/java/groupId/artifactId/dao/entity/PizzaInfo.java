package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.LocalDateTime;

public class PizzaInfo implements IPizzaInfo {
    private Long id;
    private String name;
    private String description;
    private Long size;
    private LocalDateTime creationDate;
    private LocalDateTime editDate;

    public PizzaInfo() {
    }

    public PizzaInfo(String name, String description, Long size, LocalDateTime creationDate) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.creationDate = creationDate;
    }

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Long getSize() {
        return size;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    @Override
    public String toString() {
        return "PizzaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", creationDate=" + creationDate +
                ", editDate=" + editDate +
                '}';
    }
}
