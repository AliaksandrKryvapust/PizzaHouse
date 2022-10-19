package groupId.artifactId.dao.entity.api;

import groupId.artifactId.storage.entity.api.IMenuItem;

import java.util.List;

public interface IMenu {
    List<IMenuItem> getItems();
    public Long getId();
}
