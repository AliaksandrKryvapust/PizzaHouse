package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDto;
import groupId.artifactId.core.dto.input.TokenDto;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface ITokenService {
    void add(OrderDto orderDto);

    void setTokenIdForResponse(TokenDto tokenDto);

    AtomicInteger getTokenIdForResponse();

    List<IToken> get();

    IToken getTokenIdToSend();

    Boolean isIdValid(int id);
}
