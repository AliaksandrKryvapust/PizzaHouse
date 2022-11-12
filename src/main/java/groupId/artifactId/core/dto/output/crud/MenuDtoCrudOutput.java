package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class MenuDtoCrudOutput {
    private Long id;
    private Instant createdAt;
    private Integer version;
    private String name;
    private Boolean enable;

    public MenuDtoCrudOutput() {
    }

    public MenuDtoCrudOutput(Long id, String name, Boolean enable) {
        this.id = id;
        this.name = name;
        this.enable = enable;
    }

    public MenuDtoCrudOutput(Long id, Instant createdAt, Integer version, String name, Boolean enable) {
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
}
