package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;

public class MenuDtoOutput {
    private Long id;
    private LocalDateTime createdAt;
    private Integer version;
    private String name;
    private Boolean enable;

    public MenuDtoOutput() {
    }

    public MenuDtoOutput(Long id, LocalDateTime createdAt, Integer version, String name, Boolean enable) {
        this.id = id;
        this.createdAt = createdAt;
        this.version = version;
        this.name = name;
        this.enable = enable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "MenuDtoOutput{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", enable=" + enable +
                '}';
    }
}
