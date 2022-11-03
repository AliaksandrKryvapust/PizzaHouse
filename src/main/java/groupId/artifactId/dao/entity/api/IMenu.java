package groupId.artifactId.dao.entity.api;

import groupId.artifactId.dao.entity.MenuItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IMenu {
    List<IMenuItem> getItems();

    Long getId();

    LocalDateTime getCreationDate();

    Integer getVersion();

    String getName();

    Boolean getEnable();
}
