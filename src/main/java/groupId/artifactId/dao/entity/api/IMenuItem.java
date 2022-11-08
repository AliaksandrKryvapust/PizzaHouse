package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface IMenuItem {
    Long getId();

    IPizzaInfo getInfo();

    Double getPrice();

    Long getPizzaInfoId();

    Instant getCreationDate();

    Integer getVersion();

    Long getMenuId();
}
