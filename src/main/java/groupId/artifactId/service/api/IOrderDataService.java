package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface IOrderDataService {
    void add(IToken token);

    void addOrderStage(OrderStageDtoWithId orderStageDtoWithId);

    List<IOrderData> get();

    Boolean isIdValid(int id);
}
