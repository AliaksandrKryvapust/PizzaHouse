package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.OrderDto;

public interface ITokenValidator {
    void validateToken(OrderDto orderDto);
}
