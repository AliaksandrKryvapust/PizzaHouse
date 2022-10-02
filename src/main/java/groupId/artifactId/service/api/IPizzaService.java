package groupId.artifactId.service.api;

import groupId.artifactId.core.api.IPizza;

import java.util.Optional;

public interface IPizzaService extends IEssenceService<IPizza> {
    Optional<IPizza> getByName(String name);
}
