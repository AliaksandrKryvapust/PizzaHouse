package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface ITicket {
    Long getId();

    Long getOrderId();

    Instant getCreateAt();

    Integer getVersion();

    IOrder getOrder();
}
