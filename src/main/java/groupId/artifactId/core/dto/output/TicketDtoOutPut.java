package groupId.artifactId.core.dto.output;

import java.time.Instant;

public class TicketDtoOutPut {
    private OrderDtoOutput order;
    private Long id;
    private Long orderId;
    private Instant createAt;
    private Integer version;

    public TicketDtoOutPut() {
    }

    public TicketDtoOutPut(OrderDtoOutput order, Long id, Long orderId, Instant createAt, Integer version) {
        this.order = order;
        this.id = id;
        this.orderId = orderId;
        this.createAt = createAt;
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

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
