package groupId.artifactId.storage.api;

import groupId.artifactId.storage.entity.api.ICompletedOrder;

import java.util.Optional;

public interface ICompletedOrderStorage extends IEssenceStorage<ICompletedOrder>{
    void add(ICompletedOrder completedOrder);
    Optional<ICompletedOrder> getById(int id);
    Boolean isIdExist(int id);
}
