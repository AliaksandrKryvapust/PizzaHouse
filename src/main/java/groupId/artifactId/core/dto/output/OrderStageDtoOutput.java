package groupId.artifactId.core.dto.output;

import java.time.Instant;

public class OrderStageDtoOutput {
    private Long id;
    private Long orderDataId;
    private String description;
    private Instant createdAt;
    private Integer version;

    public OrderStageDtoOutput() {
    }

    public OrderStageDtoOutput(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public OrderStageDtoOutput(Long id, Long orderDataId, String description, Instant createdAt, Integer version) {
        this.id = id;
        this.orderDataId = orderDataId;
        this.description = description;
        this.createdAt = createdAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderDataId() {
        return orderDataId;
    }

    public void setOrderDataId(Long orderDataId) {
        this.orderDataId = orderDataId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
