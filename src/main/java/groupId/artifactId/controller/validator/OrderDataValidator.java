package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.OrderDataDto;
import groupId.artifactId.core.dto.input.OrderStageDtoWithId;
import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.service.OrderService;
import groupId.artifactId.service.api.IOrderService;

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
    public void validate(OrderDataDto orderDataDto) {
        if (orderDataDto == null) {
            throw new IllegalStateException("Error code 500. None of OrderDataDto have been sent as an input");
        }
        if (orderDataDto.getToken() == null) {
            throw new IllegalStateException("Error code 500. None of Ticket in OrderDataDto have been sent as an input");
        }
        if (orderDataDto.getToken().getId() == null) {
            throw new IllegalStateException("Error code 500. None of Ticket id have been sent as an input");
        }
        if (orderDataDto.getDone() == null) {
            throw new IllegalStateException("Error code 500. None of OrderData done state have been sent as an input");
        }
    }

    @Override
    public void validateOrderStage(OrderStageDtoWithId orderStage) {
        if (orderStage == null) {
            throw new IllegalStateException("Error code 500. None of OrderStageDtoWithId have been sent as an input");
        }
        if (orderStage.getDescription() == null || orderStage.getDescription().isBlank()) {
            throw new IllegalArgumentException("Error code 400. The stage description is not valid");
        }
        if (LocalTime.now().isBefore(orderStage.getTime())) {
            throw new IllegalArgumentException("Error code 400. The time is not valid");
        }
//        IOrderService tokenService = OrderService.getInstance();
//        if (!tokenService.isIdValid(orderStage.getId())) {
//            throw new IllegalArgumentException("Error code 400. There is no order with such id");
//        }
    }
}
