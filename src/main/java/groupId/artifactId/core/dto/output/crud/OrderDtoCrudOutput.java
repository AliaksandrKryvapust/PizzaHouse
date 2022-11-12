package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class OrderDtoCrudOutput {
    private Long id;
    private Instant creationDate;
    private Integer version;

    public OrderDtoCrudOutput() {
    }

    public OrderDtoCrudOutput(Long id) {
        this.id = id;
    }

    public OrderDtoCrudOutput(Long id, Instant creationDate, Integer version) {
        this.id = id;
        this.creationDate = creationDate;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
