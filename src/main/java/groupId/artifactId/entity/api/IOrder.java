package groupId.artifactId.entity.api;

import java.util.concurrent.atomic.AtomicInteger;

public interface IOrder {
    AtomicInteger getId();

    double getCost();

    IOrderData getOrderData();

    IToken getToken();
}
