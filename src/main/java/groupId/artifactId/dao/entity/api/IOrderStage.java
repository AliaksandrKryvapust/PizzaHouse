package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IOrderStage {
    Long getId();

    Long getOrderDataId();

    String getDescription();

    Instant getCreationDate();

    Integer getVersion();
}
