package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface IToken {
    Long getId();

    Long getOrderId();

    LocalDateTime getCreateAt();

    Integer getVersion();

    IOrder getOrder();
}
