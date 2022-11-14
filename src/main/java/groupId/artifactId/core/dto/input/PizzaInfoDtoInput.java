package groupId.artifactId.core.dto.input;

public class PizzaInfoDtoInput {
    private String name;
    private String description;
    private Integer size;

    public PizzaInfoDtoInput() {
    }

    public PizzaInfoDtoInput(String name, String description, Integer size) {
        this.name = name;
        this.description = description;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PizzaInfoDtoInput{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                '}';
    }
}
