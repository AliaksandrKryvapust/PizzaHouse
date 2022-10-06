package groupId.artifactId.service;

import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.service.api.IOrderDataValidator;
import groupId.artifactId.service.api.ITokenService;

import java.time.LocalTime;

public class OrderDataValidator implements IOrderDataValidator {
    private static OrderDataValidator firstInstance = null;

    public static OrderDataValidator getInstance() {
        synchronized (OrderDataValidator.class) {
            if (firstInstance == null) {
                firstInstance = new OrderDataValidator();
            }
            return firstInstance;
        }
    }
    @Override
    public void validateOrderStage(OrderStageDtoWithId orderStage) {
        if (orderStage == null) {
            throw new IllegalStateException("Error code 500. None of OrderDto have been sent as an input");
        }
        if (orderStage.getDescription() == null || orderStage.getDescription().isBlank()) {
            throw new IllegalArgumentException("Error code 400. The stage description is not valid");
        }
        if (LocalTime.now().isBefore(orderStage.getTime())){
            throw new IllegalArgumentException("Error code 400. The time is not valid");
        }
        ITokenService tokenService = TokenService.getInstance();
        if (!tokenService.isIdValid(orderStage.getId())){
            throw new IllegalArgumentException("Error code 400. There is no order with such id");
        }
    }
}
