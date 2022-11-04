package groupId.artifactId.dao.api;

import java.util.List;

public interface IDao<TYPE> {
    TYPE save(TYPE type);

    List<TYPE> get();

    TYPE get(Long id);

    void delete(Long id, Integer version, Boolean delete);
}
