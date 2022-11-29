package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface ITicket {
    Long getId();
    IOrder getOrder();
    Instant getCreateAt();
}
