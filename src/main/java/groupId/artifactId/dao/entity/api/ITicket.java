package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface ITicket {
    Long getId();

    Long getOrderId();

    LocalDateTime getCreateAt();

    Integer getVersion();

    IOrder getOrder();
}
