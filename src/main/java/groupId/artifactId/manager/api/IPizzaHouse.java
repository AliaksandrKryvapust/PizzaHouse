package groupId.artifactId.manager.api;

import groupId.artifactId.entity.api.*;

import java.util.List;

public interface IPizzaHouse {
    IMenu getMenu();
    IToken makeOrder(List<IPizza> order);
    IOrderData getOrderDataByToken(IToken token);
    IOrder getByToken(IToken token);
}
