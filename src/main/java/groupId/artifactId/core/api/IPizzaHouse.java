package groupId.artifactId.core.api;

import groupId.artifactId.core.entity.api.IOrder;
import groupId.artifactId.core.entity.api.IOrderData;
import groupId.artifactId.core.entity.api.IPizza;
import groupId.artifactId.core.entity.api.IToken;

import java.util.List;

public interface IPizzaHouse {
    List<IPizza> getMenu();
    IToken makeOrder(List<IPizza> order);
    IOrderData getOrderDataByToken(IToken token);
    IOrder getByToken(IToken token);
}
