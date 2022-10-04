package groupId.artifactId.entity;

import groupId.artifactId.entity.api.IPizza;

public class Pizza implements IPizza {
    private String name;
    private Integer size;

    public Pizza(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        return size;
    }
}
