package groupId.artifactId.service.api;

import java.util.List;

public interface IService<TYPE,TYPE2> {
    void save(TYPE2 type);

    void update(TYPE2 type, String id, String version);

    List<TYPE> get();

    TYPE get(Long id);

    void delete(String id, String version);
}
