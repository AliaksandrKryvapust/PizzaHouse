package groupId.artifactId.service;

import groupId.artifactId.core.dto.OrderDto;
import groupId.artifactId.core.mapper.TokenMapper;
import groupId.artifactId.service.api.ITokenService;
import groupId.artifactId.service.api.ITokenValidator;
import groupId.artifactId.storage.api.ITokenStorage;
import groupId.artifactId.storage.api.StorageFactory;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;

public class TokenService implements ITokenService {
    private static TokenService firstInstance=null;
    private final ITokenStorage storage;
    private final ITokenValidator validator;

    private TokenService() {
        this.storage = StorageFactory.getInstance().getTokenStorage();
        this.validator = TokenValidator.getInstance();
    }

    public static TokenService getInstance() {
        synchronized (TokenService.class) {
            if (firstInstance == null) {
                firstInstance = new TokenService();
            }
        }
        return firstInstance;
    }

    @Override
    public void add(OrderDto orderDto) {
        this.validator.validateToken(orderDto);
        this.storage.add(TokenMapper.orderMapping(orderDto));
    }

    @Override
    public List<IToken> get() {
        return this.storage.get();
    }

    @Override
    public IToken getTokenIdToSend() {
       if (this.isIdValid(this.storage.getTokenIdToSend().get())) {
           return this.storage.getById(this.storage.getTokenIdToSend().get()).orElse(null);
       } else throw new RuntimeException("There is no Token with such id to return");
    }

    @Override
    public Boolean isIdValid(int id) {
        return this.storage.isIdExist(id);
    }
}
