package groupId.artifactId.service;

import groupId.artifactId.controller.validator.TokenValidator;
import groupId.artifactId.core.dto.input.OrderDto;
import groupId.artifactId.core.dto.input.TokenDto;
import groupId.artifactId.core.mapper.TokenMapper;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.ITokenService;
import groupId.artifactId.controller.validator.api.ITokenValidator;
import groupId.artifactId.storage.api.ITokenStorage;
import groupId.artifactId.storage.api.StorageFactory;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenService implements ITokenService {
    private static TokenService firstInstance = null;
    private final ITokenStorage storage;
    private final ITokenValidator validator;
    private final AtomicInteger tokenIdForResponse = new AtomicInteger(0);

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
    public void setTokenIdForResponse(TokenDto tokenDto) {
        this.tokenIdForResponse.set(tokenDto.getId());
    }

    @Override
    public AtomicInteger getTokenIdForResponse() {
        return tokenIdForResponse;
    }

    @Override
    public void add(OrderDto orderDto) {
        this.validator.validateToken(orderDto);
        this.storage.add(TokenMapper.orderMapping(orderDto));
        IOrderDataService orderData = OrderDataService.getInstance();
        orderData.addToken(storage.getLastToken());
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
