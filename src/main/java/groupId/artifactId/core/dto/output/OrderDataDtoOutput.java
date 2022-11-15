package groupId.artifactId.core.dto.output;

import java.time.Instant;
import java.util.List;

public class OrderDataDtoOutput {
    private TicketDtoOutput ticket;
    private List<OrderStageDtoOutput> orderHistory;
    private Long id;
    private Long ticketId;
    private Boolean done;
    private Instant createdAt;
    private Integer version;

    public OrderDataDtoOutput() {
    }

    public OrderDataDtoOutput(Long id, Long ticketId, Boolean done) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderDataDtoOutput(Long id, Long ticketId, Boolean done, Instant createdAt, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
        this.createdAt = createdAt;
        this.version = version;
    }

    public OrderDataDtoOutput(TicketDtoOutput ticket, List<OrderStageDtoOutput> orderHistory, Long id, Long ticketId,
                              Boolean done, Instant createdAt, Integer version) {
        this.ticket = ticket;
        this.orderHistory = orderHistory;
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
        this.createdAt = createdAt;
        this.version = version;
    }

    public TicketDtoOutput getTicket() {
        return ticket;
    }

    public void setTicket(TicketDtoOutput ticket) {
        this.ticket = ticket;
    }

    public List<OrderStageDtoOutput> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderStageDtoOutput> orderHistory) {
        this.orderHistory = orderHistory;
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
