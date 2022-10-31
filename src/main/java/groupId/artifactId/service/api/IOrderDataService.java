package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDataDto;
import groupId.artifactId.core.dto.input.OrderStageDtoWithId;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;
import java.util.Optional;

public interface IOrderDataService {
    void addToken(IToken token);
    void update(OrderDataDto orderDataDto);

    void addOrderStage(OrderStageDtoWithId orderStageDtoWithId);

    List<IOrderData> get();

    Optional<IOrderData> getById(int id);

    Boolean isIdValid(int id);
}
