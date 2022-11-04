package groupId.artifactId.service.api;

import java.util.List;

public interface IService<TYPE,TYPE2> {
    TYPE save(TYPE2 type);

    List<TYPE> get();

    TYPE get(Long id);

    void delete(String id, String version, String delete);
}
