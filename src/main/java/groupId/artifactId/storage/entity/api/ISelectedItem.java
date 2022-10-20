package groupId.artifactId.storage.entity.api;

import groupId.artifactId.dao.entity.api.IMenuItem;

public interface ISelectedItem {
    IMenuItem getItem();

    int getCount();
}
