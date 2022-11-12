package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class TicketDtoCrudOutput {
    private Long id;
    private Long orderId;
    private Instant createAt;
    private Integer version;

    public TicketDtoCrudOutput() {
    }

    public TicketDtoCrudOutput(Long id, Long orderId) {
        this.id = id;
        this.orderId = orderId;
    }

    public TicketDtoCrudOutput(Long id, Long orderId, Instant createAt, Integer version) {
        this.id = id;
        this.orderId = orderId;
        this.createAt = createAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
