package groupId.artifactId.dao.api;

import javax.persistence.EntityManager;

public interface IDaoUpdate<TYPE> {
    TYPE update(TYPE type, Long id, Integer version, EntityManager entityManager);
}
