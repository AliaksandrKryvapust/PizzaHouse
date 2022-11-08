package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface ISelectedItem {
    IMenuItem getItem();

    Long getId();

    Long getMenuItemId();

    Long getOrderId();

    Integer getCount();

    Instant getCreateAt();

    Integer getVersion();
}
