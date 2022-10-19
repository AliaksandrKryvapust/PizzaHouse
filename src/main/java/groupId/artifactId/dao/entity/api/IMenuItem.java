package groupId.artifactId.dao.entity.api;

import groupId.artifactId.storage.entity.api.IPizzaInfo;

public interface IMenuItem {
    IPizzaInfo getInfo();

    double getPrice();
}
