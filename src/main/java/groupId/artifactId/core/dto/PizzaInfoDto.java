package groupId.artifactId.core.dto;

import groupId.artifactId.storage.entity.api.IPizzaInfo;

public class PizzaInfoDto implements IPizzaInfo {
    private String name;
    private String description;
    private Integer size;

    public PizzaInfoDto() {
    }

    public PizzaInfoDto(String name, String description, Integer size) {
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
