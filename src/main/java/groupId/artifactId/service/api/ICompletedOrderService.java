package groupId.artifactId.service.api;

import groupId.artifactId.storage.entity.api.ICompletedOrder;
import groupId.artifactId.storage.entity.api.IOrderData;

import java.util.List;
import java.util.Optional;

public interface ICompletedOrderService {

    void add(IOrderData orderData);

    List<ICompletedOrder> get();

    Optional<ICompletedOrder> getById(int id);

    Boolean isIdValid(int id);
}
