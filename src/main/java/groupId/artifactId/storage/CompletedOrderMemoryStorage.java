package groupId.artifactId.storage;

import groupId.artifactId.storage.api.ICompletedOrderStorage;
import groupId.artifactId.storage.entity.api.ICompletedOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompletedOrderMemoryStorage implements ICompletedOrderStorage {
    final List<ICompletedOrder> completedOrderList = new ArrayList<>();

    public CompletedOrderMemoryStorage() {
    }

    @Override
    public List<ICompletedOrder> get() {
        return this.completedOrderList;
    }

    @Override
    public void add(ICompletedOrder completedOrder) {
        this.completedOrderList.add(completedOrder);
    }

    @Override
    public Optional<ICompletedOrder> getById(int id) {
        return this.completedOrderList.stream().filter((i)->i.getToken().getId()==id).findFirst();
    }

    @Override
    public Boolean isIdExist(int id) {
        return this.completedOrderList.stream().anyMatch((i)->i.getToken().getId()==id);
    }
}
