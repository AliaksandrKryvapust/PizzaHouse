package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IOrderStage;

import java.util.Optional;

public interface IOrderDataStorage extends IEssenceStorage<IOrderData> {
    void addOrderStage(IOrderStage orderStage, int tokenId);
    void updateOrderData(IOrderData orderData);
    Optional<IOrderData> getById(int id);
    Boolean isIdExist(int id);
}
