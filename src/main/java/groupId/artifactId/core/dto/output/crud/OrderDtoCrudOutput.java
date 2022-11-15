package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class OrderDtoCrudOutput {
    private Long id;
    private Instant createdAt;
    private Integer version;

    public OrderDtoCrudOutput() {
    }

    public OrderDtoCrudOutput(Long id) {
        this.id = id;
    }

    public OrderDtoCrudOutput(Long id, Instant createdAt, Integer version) {
        this.id = id;
        this.createdAt = createdAt;
        this.version = version;
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
}
