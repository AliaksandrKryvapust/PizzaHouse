package groupId.artifactId.dao.entity.api;

import java.time.Instant;
import java.util.List;

public interface IOrderData {
    ITicket getTicket();

    List<IOrderStage> getOrderHistory();

    Long getId();

    Long getTicketId();

    Boolean isDone();

    Instant getCreationDate();

    Integer getVersion();
}
