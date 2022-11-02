package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface IMenuItem {
    Long getId();

    IPizzaInfo getInfo();

    Double getPrice();

    Long getPizzaInfoId();

    LocalDateTime getCreationDate();

    Integer getVersion();

    Long getMenuId();
}
