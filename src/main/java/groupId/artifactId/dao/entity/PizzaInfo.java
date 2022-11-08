package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.time.Instant;

public class PizzaInfo implements IPizzaInfo {
    private Long id;
    private String name;
    private String description;
    private Integer size;
    private Instant creationDate;
    private Integer version;

    public PizzaInfo() {
    }

    public PizzaInfo(String name, String description, Integer size) {
        this.name = name;
        this.description = description;
        this.size = size;
    }

    public PizzaInfo(Long id, String name, String description, Integer size) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
    }

    public PizzaInfo(Long id, String name, String description, Integer size, Instant creationDate, Integer version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.creationDate = creationDate;
        this.version = version;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
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

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
    public Integer getSize() {
        return size;
    }

    @Override
    public Instant getCreationDate() {
        return creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "PizzaInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
