package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;
import java.util.List;

public interface ICompletedOrder {
    ITicket getTicket();

    List<IPizza> getItems();

    Long getId();

    Long getTicketId();

    LocalDateTime getCreationDate();

    Integer getVersion();
}
