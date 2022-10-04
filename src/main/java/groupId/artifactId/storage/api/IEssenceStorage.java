package groupId.artifactId.storage.api;

import java.util.List;

public interface IEssenceStorage<TYPE> {
    List<TYPE> get();

    void save (TYPE type);
}
