package groupId.artifactId.dao.entity.api;

import java.time.Instant;
import java.util.List;

public interface IMenu {
    List<IMenuItem> getItems();

    Long getId();

    Instant getCreationDate();

    Integer getVersion();

    String getName();

    Boolean getEnable();
}
