package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IOrderStage {
    Long getId();
    String getDescription();
    Instant getCreationDate();
}
