package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrder {
    List<ISelectedItem> getSelectedItems();

    Long getId();

    LocalDateTime getCreationDate();

    Integer getVersion();
}
