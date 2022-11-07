package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface IPizza {
    Long getId();

    Long getCompletedOrderId();

    String getName();

    Integer getSize();

    LocalDateTime getCreationDate();

    Integer getVersion();
}
