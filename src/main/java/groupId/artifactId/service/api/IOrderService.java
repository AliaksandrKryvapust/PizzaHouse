package groupId.artifactId.service.api;

import groupId.artifactId.entity.api.IOrder;
import groupId.artifactId.entity.api.IToken;

import java.util.Optional;

public interface IOrderService extends IEssenceService<IOrder> {
    Optional<IOrder> getById(int id);
    IToken getToken(IOrder order);
}
