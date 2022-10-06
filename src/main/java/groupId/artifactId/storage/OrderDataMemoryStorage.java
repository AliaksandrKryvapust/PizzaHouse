package groupId.artifactId.storage;

import groupId.artifactId.storage.api.IOrderDataStorage;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IOrderStage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OrderDataMemoryStorage implements IOrderDataStorage {

    private final List<IOrderData> orderDataList = new ArrayList<>();

    public OrderDataMemoryStorage() {
    }

    @Override
    public List<IOrderData> get() {
        return this.orderDataList;
    }

    @Override
    public void add(IOrderData orderData) {
    this.orderDataList.add(orderData);
    }

    @Override
    public void addOrderStage(IOrderStage orderStage, int tokenId) {
    Objects.requireNonNull(this.getById(tokenId).orElse(null)).addOrderStage(orderStage);
    }

    @Override
    public Optional<IOrderData> getById(int id) {
        return this.orderDataList.stream().filter((i)->i.getToken().getId()==id).findFirst();
    }

    @Override
    public Boolean isIdExist(int id) {
        return orderDataList.stream().anyMatch((i)->i.getToken().getId()==id);
    }
}
