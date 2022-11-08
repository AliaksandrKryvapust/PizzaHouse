package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrderStage;

import java.time.Instant;

public class OrderStage implements IOrderStage {
    private Long id;
    private Long orderDataId;
    private String description;
    private Instant creationDate;
    private Integer version;

    public OrderStage() {
    }

    public OrderStage(String description) {
        this.description = description;
    }

    public OrderStage(Long orderDataId, String description) {
        this.orderDataId = orderDataId;
        this.description = description;
    }

    public OrderStage(Long id, Long orderDataId, String description) {
        this.id = id;
        this.orderDataId = orderDataId;
        this.description = description;
    }

    public OrderStage(Long id, Long orderDataId, String description, Instant creationDate, Integer version) {
        this.id = id;
        this.orderDataId = orderDataId;
        this.description = description;
        this.creationDate = creationDate;
        this.version = version;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getOrderDataId() {
        return orderDataId;
    }

    public void setOrderDataId(Long orderDataId) {
        this.orderDataId = orderDataId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrderStage{" +
                "id=" + id +
                ", orderDataId=" + orderDataId +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
