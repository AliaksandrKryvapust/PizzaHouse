package groupId.artifactId.entity.api;

import groupId.artifactId.storage.entity.api.IMenuItem;

public interface ISelectedItem {
    IMenuItem getItem();

    int getCount();
}
