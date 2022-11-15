package groupId.artifactId.core.dto.output;

import java.time.Instant;
import java.util.List;

public class CompletedOrderDtoOutput {
    private TicketDtoOutput ticket;
    private List<PizzaDtoOutput> items;
    private Long id;
    private Long ticketId;
    private Instant createdAt;
    private Integer version;

    public CompletedOrderDtoOutput() {
    }

    public CompletedOrderDtoOutput(TicketDtoOutput ticket, List<PizzaDtoOutput> items, Long id, Long ticketId,
                                   Instant createdAt, Integer version) {
        this.ticket = ticket;
        this.items = items;
        this.id = id;
        this.ticketId = ticketId;
        this.createdAt = createdAt;
        this.version = version;
    }

    public TicketDtoOutput getTicket() {
        return ticket;
    }

    public void setTicket(TicketDtoOutput ticket) {
        this.ticket = ticket;
    }

    public List<PizzaDtoOutput> getItems() {
        return items;
    }

    public void setItems(List<PizzaDtoOutput> items) {
        this.items = items;
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
