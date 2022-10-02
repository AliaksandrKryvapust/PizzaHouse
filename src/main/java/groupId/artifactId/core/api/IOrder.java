package groupId.artifactId.core.api;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public interface IOrder {
    AtomicInteger getId();
    double getCost();
    LocalDateTime getOrderTime();
}
