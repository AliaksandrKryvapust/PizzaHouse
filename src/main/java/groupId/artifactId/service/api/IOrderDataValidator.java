package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderDataDto;
import groupId.artifactId.core.dto.OrderStageDtoWithId;

public interface IOrderDataValidator {
    void validate(OrderDataDto orderDataDto);
    void validateOrderStage(OrderStageDtoWithId orderStage);
}
