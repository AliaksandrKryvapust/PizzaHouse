package groupId.artifactId.core.dto.input;

public class PizzaInfoDto {
    private String name;
    private String description;
    private Integer size;
    private Long id;
    private Integer version;

    public PizzaInfoDto() {
    }

    public PizzaInfoDto(String name, String description, Integer size) {
        this.name = name;
        this.description = description;
        this.size = size;
    }

    public PizzaInfoDto(String name, String description, Integer size, Long id) {
        this.name = name;
        this.description = description;
        this.size = size;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getSize() {
        return Long.valueOf(size);
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }
}
