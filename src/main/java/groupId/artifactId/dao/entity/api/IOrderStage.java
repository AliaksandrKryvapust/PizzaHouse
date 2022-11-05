package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface IOrderStage {
    Long getId();

    Long getOrderDataId();

    String getDescription();

    LocalDateTime getCreationDate();

    Integer getVersion();
}
