package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;

public class PizzaDtoOutput {
    private Long id;
    private Long completedOrderId;
    private String name;
    private Integer size;
    private LocalDateTime creationDate;
    private Integer version;

    public PizzaDtoOutput() {
    }

    public PizzaDtoOutput(Long id, Long completedOrderId, String name, Integer size, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.completedOrderId = completedOrderId;
        this.name = name;
        this.size = size;
        this.creationDate = creationDate;
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
