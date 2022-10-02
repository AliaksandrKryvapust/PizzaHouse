package groupId.artifactId.core.entity.api;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public interface IOrder {
    AtomicInteger getId();
    double getCost();
    IOrderData getOrderData();
    IToken getToken();

}
