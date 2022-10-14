package groupId.artifactId.core.dto;

public class PizzaInfoDto {
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSize() {
        return size;
    }
}
