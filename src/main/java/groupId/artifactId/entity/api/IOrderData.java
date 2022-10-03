package groupId.artifactId.entity.api;

import java.time.LocalDateTime;

public interface IOrderData {
    LocalDateTime getOrderAcceptedTime();

    LocalDateTime getPizzaStartCookingTime();

    LocalDateTime getPackingTime();

    LocalDateTime getOrderReadyTime();

    EOrderType getOrderType();

    EPaymentType getPaymentType();
}
