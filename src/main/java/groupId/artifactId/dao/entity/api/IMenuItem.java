package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IMenuItem {
    Long getId();
    IPizzaInfo getPizzaInfo();
    Double getPrice();
    Instant getCreationDate();
    Integer getVersion();
}
