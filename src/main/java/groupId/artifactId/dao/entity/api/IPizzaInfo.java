package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IPizzaInfo {
    Long getId();

    String getName();

    String getDescription();

    Integer getSize();

    Instant getCreationDate();

    Integer getVersion();

}
