package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.dao.entity.api.ITicket;

import java.time.LocalDateTime;
import java.util.List;

public class CompletedOrder implements ICompletedOrder {
    private ITicket ticket;
    private List<IPizza> items;
    private Long id;
    private Long ticketId;
    private LocalDateTime creationDate;
    private Integer version;

    public CompletedOrder() {
    }

    public CompletedOrder(Long ticketId) {
        this.ticketId = ticketId;
    }

    public CompletedOrder(Long id, Long ticketId) {
        this.id = id;
        this.ticketId = ticketId;
    }

    public CompletedOrder(ITicket ticket, List<IPizza> items, Long ticketId) {
        this.ticket = ticket;
        this.items = items;
        this.ticketId = ticketId;
    }

    public CompletedOrder(Long id, Long ticketId, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.creationDate = creationDate;
        this.version = version;
    }

    public CompletedOrder(ITicket ticket, List<IPizza> items, Long id, Long ticketId, LocalDateTime creationDate, Integer version) {
        this.ticket = ticket;
        this.items = items;
        this.id = id;
        this.ticketId = ticketId;
        this.creationDate = creationDate;
        this.version = version;
    }

    @Override
    public ITicket getTicket() {
        return ticket;
    }

    public void setTicket(ITicket ticket) {
        this.ticket = ticket;
    }

    @Override
    public List<IPizza> getItems() {
        return items;
    }

    public void setItems(List<IPizza> items) {
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
