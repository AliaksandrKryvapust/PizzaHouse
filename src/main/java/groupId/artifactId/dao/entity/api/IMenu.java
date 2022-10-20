package groupId.artifactId.dao.entity.api;

import groupId.artifactId.dao.entity.MenuItem;

import java.util.List;

public interface IMenu {
    List<MenuItem> getItems();
    public Long getId();
}
