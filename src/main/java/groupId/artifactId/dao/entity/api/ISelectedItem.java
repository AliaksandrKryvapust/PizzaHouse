package groupId.artifactId.dao.entity.api;

import java.time.Instant;

public interface ISelectedItem {
    Long getId();
    IMenuItem getMenuItem();
    Integer getCount();
    Instant getCreateAt();
}
