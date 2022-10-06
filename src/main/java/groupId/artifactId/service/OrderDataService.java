package groupId.artifactId.service;

import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderDataValidator;
import groupId.artifactId.storage.api.IOrderDataStorage;
import groupId.artifactId.storage.api.StorageFactory;
import groupId.artifactId.storage.entity.OrderData;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IToken;

import java.util.List;
import java.util.Optional;

public class OrderDataService implements IOrderDataService {
    private static OrderDataService firstInstance = null;
    private final IOrderDataStorage storage;
    private final IOrderDataValidator validator;

    private OrderDataService() {
        this.storage = StorageFactory.getInstance().getOrderDataStorage();
        this.validator = OrderDataValidator.getInstance();
    }

    public static OrderDataService getInstance() {
        synchronized (OrderDataService.class) {
            if (firstInstance == null) {
                firstInstance = new OrderDataService();
            }
        }
        return firstInstance;
    }

    @Override
    public void add(IToken token) {
        this.storage.add(new OrderData(token, false));
    }

    @Override
    public void addOrderStage(OrderStageDtoWithId orderStageDtoWithId) {
        this.validator.validateOrderStage(orderStageDtoWithId);
        this.storage.addOrderStage(OrderDataMapper.OrderStageWithIdMapping(orderStageDtoWithId), orderStageDtoWithId.getId());
    }

    @Override
    public List<IOrderData> get() {
        return this.storage.get();
    }

    @Override
    public Optional<IOrderData> getById(int id) {
        return this.storage.getById(id);
    }

    @Override
    public Boolean isIdValid(int id) {
        return this.storage.isIdExist(id);
    }
}
