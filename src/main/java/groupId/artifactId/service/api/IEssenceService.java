package groupId.artifactId.service.api;

import java.util.List;

public interface IEssenceService<TYPE> {
    List<TYPE> get();

    void add(TYPE type);
}
