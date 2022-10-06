package groupId.artifactId.storage;

import groupId.artifactId.storage.api.ITokenStorage;
import groupId.artifactId.storage.entity.Token;
import groupId.artifactId.storage.entity.api.IToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenMemoryStorage implements ITokenStorage {
    private final List<IToken> tokenList = new ArrayList<>();

    private final AtomicInteger tokenIdToSend = new AtomicInteger(0);

    public TokenMemoryStorage() {
    }

    @Override
    public List<IToken> get() {
        return this.tokenList;
    }

    @Override
    public AtomicInteger getTokenIdToSend() {
        return tokenIdToSend;
    }

    @Override
    public void add(IToken token) {
        if (token.getId() != null) {
            throw new IllegalStateException("Error code 500. Token id should be empty");
        }
        if (token.getCreatAt() != null) {
            throw new IllegalStateException("Error code 500. Token create date should be empty");
        }
        Token temp = (Token) token;
        temp.setId(tokenList.size() + 1);
        temp.setCreateAt(LocalDateTime.now());
        this.tokenIdToSend.set(token.getId());
        this.tokenList.add(temp);
    }

    @Override
    public Optional<IToken> getById(int id) {
        return this.tokenList.stream().filter((i) -> i.getId() == id).findFirst();
    }

    @Override
    public IToken getLastToken() {
        return this.getById(this.tokenList.size()+1).orElse(null);
    }

    @Override
    public Boolean isIdExist(int id) {
        return this.tokenList.stream().anyMatch((i) -> i.getId() == id);
    }

}
