package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IPizza {
    Long getId();

    Long getCompletedOrderId();

    String getName();

    Integer getSize();

    Instant getCreationDate();

    Integer getVersion();
}
