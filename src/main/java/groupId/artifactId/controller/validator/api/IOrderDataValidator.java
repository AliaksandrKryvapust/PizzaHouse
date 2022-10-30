package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.input.OrderDataDto;
import groupId.artifactId.core.dto.input.OrderStageDtoWithId;

public interface IOrderDataValidator {
    void validate(OrderDataDto orderDataDto);
    void validateOrderStage(OrderStageDtoWithId orderStage);
}
