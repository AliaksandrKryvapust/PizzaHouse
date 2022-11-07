package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;
import java.util.List;

public class CompletedOrderDtoOutput {
    private TicketDtoOutPut ticket;
    private List<PizzaDtoOutput> items;
    private Long id;
    private Long ticketId;
    private LocalDateTime creationDate;
    private Integer version;

    public CompletedOrderDtoOutput() {
    }

    public CompletedOrderDtoOutput(TicketDtoOutPut ticket, List<PizzaDtoOutput> items, Long id, Long ticketId,
                                   LocalDateTime creationDate, Integer version) {
        this.ticket = ticket;
        this.items = items;
        this.id = id;
        this.ticketId = ticketId;
        this.creationDate = creationDate;
        this.version = version;
    }

    public TicketDtoOutPut getTicket() {
        return ticket;
    }

    public void setTicket(TicketDtoOutPut ticket) {
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
