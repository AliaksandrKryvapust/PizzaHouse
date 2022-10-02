package groupId.artifactId.service.api;

import groupId.artifactId.core.entity.api.IToken;

import java.util.Optional;

public interface ITTokenService extends IEssenceService<IToken> {
    Optional<IToken> getById(int id);
}
