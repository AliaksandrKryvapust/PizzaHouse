package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderDto;
import groupId.artifactId.core.dto.TokenDto;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public interface ITokenService {
    void add(OrderDto orderDto);

    void setTokenIdForResponse(TokenDto tokenDto);

    List<IToken> get();

    IToken getTokenIdToSend();

    Boolean isIdValid(int id);
}
