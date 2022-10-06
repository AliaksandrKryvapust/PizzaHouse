package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderStageDtoWithId;

public interface IOrderDataValidator {
    void validateOrderStage(OrderStageDtoWithId orderStage);
}
