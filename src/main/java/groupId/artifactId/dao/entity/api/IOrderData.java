package groupId.artifactId.dao.entity.api;

import java.time.Instant;
import java.util.List;

public interface IOrderData {
    Long getId();
    ITicket getTicket();
    List<IOrderStage> getOrderHistory();
    Boolean getDone();
    Instant getCreationDate();
}
