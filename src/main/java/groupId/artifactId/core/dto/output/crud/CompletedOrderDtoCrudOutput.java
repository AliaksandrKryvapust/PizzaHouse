package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class CompletedOrderDtoCrudOutput {
    private Long id;
    private Long ticketId;
    private Instant creationDate;
    private Integer version;

    public CompletedOrderDtoCrudOutput() {
    }

    public CompletedOrderDtoCrudOutput(Long id, Long ticketId) {
        this.id = id;
        this.ticketId = ticketId;
    }

    public CompletedOrderDtoCrudOutput(Long id, Long ticketId, Instant creationDate, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.creationDate = creationDate;
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
