package groupId.artifactId.service;

import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.storage.api.ICompletedOrderStorage;
import groupId.artifactId.storage.api.StorageFactory;
import groupId.artifactId.storage.entity.api.ICompletedOrder;
import groupId.artifactId.storage.entity.api.IOrderData;

import java.util.List;
import java.util.Optional;

public class CompletedOrderService implements ICompletedOrderService {
    private static CompletedOrderService firstInstance = null;
    private final ICompletedOrderStorage storage;

    private CompletedOrderService() {
        this.storage = StorageFactory.getInstance().getCompletedOrderStorage();
    }

    public static CompletedOrderService getInstance() {
        synchronized (CompletedOrderService.class) {
            if (firstInstance == null) {
                firstInstance = new CompletedOrderService();
            }
        }
        return firstInstance;
    }
    @Override
    public void add(IOrderData orderData) {
    this.storage.add(CompletedOrderMapper.orderDataMapping(orderData));
    }

    @Override
    public List<ICompletedOrder> get() {
        return this.storage.get();
    }

    @Override
    public Optional<ICompletedOrder> getById(int id) {
        return this.storage.getById(id);
    }

    @Override
    public Boolean isIdValid(int id) {
        return this.storage.isIdExist(id);
    }
}
