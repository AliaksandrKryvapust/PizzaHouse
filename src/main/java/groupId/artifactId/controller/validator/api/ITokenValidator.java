package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.input.OrderDto;

public interface ITokenValidator {
    void validateToken(OrderDto orderDto);
}
