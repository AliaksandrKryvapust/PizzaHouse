package groupId.artifactId.core.api;

import java.time.LocalDateTime;

public interface IOrderData {
    LocalDateTime getOrderAcceptedTime();
    LocalDateTime getPizzaStartCookingTime();
}
