package groupId.artifactId.core.dto.output;

import java.time.Instant;

public class TicketDtoOutput {
    private OrderDtoOutput order;
    private Long id;
    private Long orderId;
    private Instant createdAt;
    private Integer version;

    public TicketDtoOutput() {
    }

    public TicketDtoOutput(OrderDtoOutput order, Long id, Long orderId, Instant createdAt, Integer version) {
        this.order = order;
        this.id = id;
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.version = version;
    }

    public OrderDtoOutput getOrder() {
        return order;
    }

    public void setOrder(OrderDtoOutput order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
