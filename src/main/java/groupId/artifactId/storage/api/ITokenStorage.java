package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.IToken;

import java.util.Optional;

public interface ITokenStorage extends IEssenceStorage<IToken> {
    Optional<IToken> getById(int id);
    Boolean isIdExist(int id);
}
