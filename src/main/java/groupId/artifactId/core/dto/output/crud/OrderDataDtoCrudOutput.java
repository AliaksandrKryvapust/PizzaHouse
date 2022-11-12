package groupId.artifactId.core.dto.output.crud;

import java.time.Instant;

public class OrderDataDtoCrudOutput {
    private Long id;
    private Long ticketId;
    private Boolean done;
    private Instant creationDate;
    private Integer version;

    public OrderDataDtoCrudOutput() {
    }

    public OrderDataDtoCrudOutput(Long id, Long ticketId, Boolean done) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderDataDtoCrudOutput(Long id, Long ticketId, Boolean done, Instant creationDate, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
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

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
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
