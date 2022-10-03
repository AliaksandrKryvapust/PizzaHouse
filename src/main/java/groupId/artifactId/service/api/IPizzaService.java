package groupId.artifactId.service.api;

import groupId.artifactId.entity.api.IPizza;

import java.util.Optional;

public interface IPizzaService extends IEssenceService<IPizza> {
    Optional<IPizza> getByName(String name);
}
