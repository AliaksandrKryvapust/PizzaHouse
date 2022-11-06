package groupId.artifactId.core.dto.output;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDataDtoOutput {
    private TicketDtoOutPut ticket;
    private List<OrderStageDtoOutput> orderHistory;
    private Long id;
    private Long ticketId;
    private Boolean done;
    private LocalDateTime creationDate;
    private Integer version;

    public OrderDataDtoOutput() {
    }

    public OrderDataDtoOutput(Long id, Long ticketId, Boolean done) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderDataDtoOutput(Long id, Long ticketId, Boolean done, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
        this.creationDate = creationDate;
        this.version = version;
    }

    public OrderDataDtoOutput(TicketDtoOutPut ticket, List<OrderStageDtoOutput> orderHistory, Long id, Long ticketId, Boolean done, LocalDateTime creationDate, Integer version) {
        this.ticket = ticket;
        this.orderHistory = orderHistory;
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
        this.creationDate = creationDate;
        this.version = version;
    }

    public TicketDtoOutPut getTicket() {
        return ticket;
    }

    public void setTicket(TicketDtoOutPut ticket) {
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
