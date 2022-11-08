package groupId.artifactId.core.dto.output;

import java.time.Instant;

public class PizzaInfoDtoOutput {
    private Long id;
    private String name;
    private String description;
    private Integer size;
    private Instant createdAt;
    private Integer version;

    public PizzaInfoDtoOutput() {
    }

    public PizzaInfoDtoOutput(Long id, String name, String description, Integer size, Instant createdAt,
                              Integer version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.size = size;
        this.createdAt = createdAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
