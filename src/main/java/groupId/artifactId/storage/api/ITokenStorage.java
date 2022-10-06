package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.IToken;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public interface ITokenStorage extends IEssenceStorage<IToken> {
    Optional<IToken> getById(int id);
    IToken getLastToken();
    Boolean isIdExist(int id);
    AtomicInteger getTokenIdToSend();
}
