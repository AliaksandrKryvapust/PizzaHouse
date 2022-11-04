package groupId.artifactId.dao.entity;

import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ITicket;

import java.time.LocalDateTime;

public class Ticket implements ITicket {
    private IOrder order;
    private Long id;
    private Long orderId;
    private LocalDateTime createAt;
    private Integer version;


    public Ticket() {
    }

    public Ticket(Long orderId) {
        this.orderId = orderId;
    }

    public Ticket(IOrder order, Long id, Long orderId, LocalDateTime createAt, Integer version) {
        this.order = order;
        this.id = id;
        this.orderId = orderId;
        this.createAt = createAt;
        this.version = version;
    }

    @Override
    public IOrder getOrder() {
        return order;
    }

    public void setOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
