package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderDto;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface ITokenService {
    void add(OrderDto orderDto);

    List<IToken> get();

    IToken getTokenIdToSend();

    Boolean isIdValid(int id);
}
