package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.IMenu;

public interface IMenuStorage extends IEssenceStorage<IMenu>{
    void saveNew (IMenu menu);
}
