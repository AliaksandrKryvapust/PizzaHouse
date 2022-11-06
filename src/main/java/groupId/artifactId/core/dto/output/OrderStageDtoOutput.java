package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;

public class OrderStageDtoOutput {
    private Long id;
    private Long orderDataId;
    private String description;
    private LocalDateTime creationDate;
    private Integer version;

    public OrderStageDtoOutput() {
    }

    public OrderStageDtoOutput(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public OrderStageDtoOutput(Long id, Long orderDataId, String description, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.orderDataId = orderDataId;
        this.description = description;
        this.creationDate = creationDate;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
