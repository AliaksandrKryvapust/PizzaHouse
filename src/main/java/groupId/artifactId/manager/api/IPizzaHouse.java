package groupId.artifactId.manager.api;

import groupId.artifactId.entity.api.*;
import groupId.artifactId.storage.entity.api.IMenu;

public interface IPizzaHouse {
    IMenu getMenu();
    IToken create(IOrder order);
    IOrderData getOrderDataByToken(IToken token);
    ICompletedOrder getByToken(IToken token);
}
