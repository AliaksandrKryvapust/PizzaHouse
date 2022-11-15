package groupId.artifactId.core.dto.output;

import java.time.Instant;

public class PizzaDtoOutput {
    private Long id;
    private Long completedOrderId;
    private String name;
    private Integer size;
    private Instant createdAt;
    private Integer version;

    public PizzaDtoOutput() {
    }

    public PizzaDtoOutput(Long id, Long completedOrderId, String name, Integer size, Instant createdAt, Integer version) {
        this.id = id;
        this.completedOrderId = completedOrderId;
        this.name = name;
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

    public Long getCompletedOrderId() {
        return completedOrderId;
    }

    public void setCompletedOrderId(Long completedOrderId) {
        this.completedOrderId = completedOrderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
