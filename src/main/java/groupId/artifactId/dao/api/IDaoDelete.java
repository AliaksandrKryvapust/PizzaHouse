package groupId.artifactId.dao.api;

import javax.persistence.EntityManager;

public interface IDaoDelete {
    void delete(Long id, Boolean delete, EntityManager entityManager);
}
