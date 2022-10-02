package groupId.artifactId.core.entity.api;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public interface IOrder {
    double getCost();
    IOrderData getOrderData();
}
