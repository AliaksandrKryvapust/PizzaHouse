package groupId.artifactId.entity;

import groupId.artifactId.entity.api.IPizzaInfo;

public class PizzaInfo implements IPizzaInfo {
    private String name;
    private String description;
    private Integer size;

    public PizzaInfo(String name, String description, Integer size) {
        this.name = name;
        this.description = description;
        this.size = size;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getSize() {
        return size;
    }
}
