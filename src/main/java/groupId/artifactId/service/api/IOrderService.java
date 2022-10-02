package groupId.artifactId.service.api;

import groupId.artifactId.core.api.IOrder;
import groupId.artifactId.core.api.IPizza;

import java.util.Optional;

public interface IOrderService extends IEssenceService<IOrder> {
    Optional<IPizza> getById(int id);
}
