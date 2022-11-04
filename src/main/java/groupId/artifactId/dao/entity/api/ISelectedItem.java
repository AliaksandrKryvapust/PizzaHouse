package groupId.artifactId.dao.entity.api;

import java.time.LocalDateTime;

public interface ISelectedItem {
    IMenuItem getItem();

    Long getId();

    Long getMenuItemId();

    Long getOrderId();

    Integer getCount();

    LocalDateTime getCreateAt();

    Integer getVersion();
}
