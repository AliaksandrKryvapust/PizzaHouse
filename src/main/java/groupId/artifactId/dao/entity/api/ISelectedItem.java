package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface ISelectedItem {
    IMenuItem getMenuItem();
    Long getId();
    Long getOrderId();
    Integer getCount();
    Instant getCreateAt();
    Integer getVersion();
}
