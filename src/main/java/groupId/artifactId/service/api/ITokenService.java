package groupId.artifactId.service.api;

import groupId.artifactId.entity.api.IToken;

import java.util.Optional;

public interface ITokenService extends IEssenceService<IToken> {
    Optional<IToken> getById(int id);
}
