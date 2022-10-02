package groupId.artifactId.core.entity.api;

import java.time.LocalDateTime;

public interface IOrderData {
    LocalDateTime getOrderAcceptedTime();
    LocalDateTime getPizzaStartCookingTime();
    LocalDateTime getPackingTime();
    LocalDateTime getOrderReadyTime();
    OrderType getOrderType();
    PaymentType getPaymentType();
}
