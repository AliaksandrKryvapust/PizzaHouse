package groupId.artifactId.dao.entity.api;

import java.util.List;

public interface IOrder {
    Long getId();
    List<ISelectedItem> getSelectedItems();
}
