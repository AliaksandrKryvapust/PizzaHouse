package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import groupId.artifactId.dao.entity.api.ITicket;

import java.time.LocalDateTime;
import java.util.List;

public class OrderData implements IOrderData {
    private ITicket ticket;
    private List<IOrderStage> orderHistory;
    private Long id;
    private Long ticketId;
    private Boolean done;
    private LocalDateTime creationDate;
    private Integer version;

    public OrderData() {
    }

    public OrderData(Long id, Boolean done) {
        this.id = id;
        this.done = done;
    }

    public OrderData(Long id, Long ticketId, Boolean done) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderData(List<IOrderStage> orderHistory, Long ticketId, Boolean done) {
        this.orderHistory = orderHistory;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderData(List<IOrderStage> orderHistory, Long id, Long ticketId, Boolean done) {
        this.orderHistory = orderHistory;
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
    }

    public OrderData(Long id, Long ticketId, Boolean done, LocalDateTime creationDate, Integer version) {
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
        this.creationDate = creationDate;
        this.version = version;
    }

    public OrderData(ITicket ticket, List<IOrderStage> orderHistory, Long id, Long ticketId, Boolean done,
                     LocalDateTime creationDate, Integer version) {
        this.ticket = ticket;
        this.orderHistory = orderHistory;
        this.id = id;
        this.ticketId = ticketId;
        this.done = done;
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
    public List<IOrderStage> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<IOrderStage> orderHistory) {
        this.orderHistory = orderHistory;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "ticket=" + ticket +
                ", orderHistory=" + orderHistory +
                ", id=" + id +
                ", ticketId=" + ticketId +
                ", done=" + done +
                ", creationDate=" + creationDate +
                ", version=" + version +
                '}';
    }
}
