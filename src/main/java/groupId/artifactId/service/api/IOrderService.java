package groupId.artifactId.service.api;

import groupId.artifactId.core.entity.api.IOrder;
import groupId.artifactId.core.entity.api.IToken;

import java.util.Optional;

public interface IOrderService extends IEssenceService<IOrder> {
    Optional<IOrder> getByToken(IToken token);
}
