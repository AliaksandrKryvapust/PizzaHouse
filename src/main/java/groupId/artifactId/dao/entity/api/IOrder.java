package groupId.artifactId.dao.entity.api;

import java.time.Instant;
import java.util.List;

public interface IOrder {
    List<ISelectedItem> getSelectedItems();

    Long getId();

    Instant getCreationDate();

    Integer getVersion();
}
