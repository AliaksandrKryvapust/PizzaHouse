package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class CompletedOrderDtoCrudOutput {
    private Long id;
    private Long ticketId;
    private Instant createdAt;
    private Integer version;

    public CompletedOrderDtoCrudOutput() {
    }

    public CompletedOrderDtoCrudOutput(Long id, Long ticketId) {
        this.id = id;
        this.ticketId = ticketId;
    }

    public CompletedOrderDtoCrudOutput(Long id, Long ticketId, Instant createdAt, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.createdAt = createdAt;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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
