package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class PizzaInfo implements IPizzaInfo {
    private Long id;
    private String name;
    private String description;
    private Long size;

    public PizzaInfo() {
    }

    public PizzaInfo(String name, String description, Long size) {
        this.name = name;
        this.description = description;
        this.size = size;
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
}
