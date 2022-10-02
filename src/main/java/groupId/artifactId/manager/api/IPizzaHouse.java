package groupId.artifactId.manager.api;

import groupId.artifactId.core.api.IOrder;
import groupId.artifactId.core.api.IOrderData;
import groupId.artifactId.core.api.IPizza;
import groupId.artifactId.core.api.IToken;

import java.util.List;

public interface IPizzaHouse {
    List<IPizza> getMenu();
    IToken makeOrder(List<IPizza> order);
    IOrderData getOrderDataByToken(IToken token);
    IOrder getByToken(IToken token);
}
