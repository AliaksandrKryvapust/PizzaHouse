package groupId.artifactId.dao.api;

import javax.persistence.EntityManager;
import java.util.List;

public interface IDao<TYPE> {
    TYPE save(TYPE type, EntityManager entityManager);

    List<TYPE> get();

    TYPE get(Long id);


}
