package groupId.artifactId.service.api;

import groupId.artifactId.core.api.IOrder;
import groupId.artifactId.core.api.IToken;

import java.util.Optional;

public interface IOrderService extends IEssenceService<IOrder> {
    Optional<IOrder> getById(int id);
    IToken getToken(IOrder order);
}
