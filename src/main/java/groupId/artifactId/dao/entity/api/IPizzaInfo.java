package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface IPizzaInfo {
    Long getId();

    String getName();

    String getDescription();

    Integer getSize();

    LocalDateTime getCreationDate();

    Integer getVersion();

}
