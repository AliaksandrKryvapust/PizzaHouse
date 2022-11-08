package groupId.artifactId.dao.entity.api;

import java.time.Instant;
import java.util.List;

public interface ICompletedOrder {
    ITicket getTicket();

    List<IPizza> getItems();

    Long getId();

    Long getTicketId();

    Instant getCreationDate();

    Integer getVersion();
}
