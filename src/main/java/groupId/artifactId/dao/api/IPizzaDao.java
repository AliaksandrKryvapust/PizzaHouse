package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.api.IPizza;

public interface IPizzaDao extends IDao<IPizza> {
    Boolean exist(Long id);
}
