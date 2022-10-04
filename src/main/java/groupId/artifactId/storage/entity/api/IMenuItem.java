package groupId.artifactId.storage.entity.api;

import groupId.artifactId.core.dto.api.IPizzaInfo;

public interface IMenuItem {
    IPizzaInfo getInfo();

    double getPrice();
}
