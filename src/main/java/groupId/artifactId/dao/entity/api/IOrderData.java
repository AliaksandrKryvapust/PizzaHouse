package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderData {
    ITicket getTicket();

    List<IOrderStage> getOrderHistory();

    Long getId();

    Long getTicketId();

    Boolean isDone();

    LocalDateTime getCreationDate();

    Integer getVersion();
}
