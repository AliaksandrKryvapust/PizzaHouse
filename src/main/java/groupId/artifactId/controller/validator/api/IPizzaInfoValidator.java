package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.input.PizzaInfoDto;

public interface IPizzaInfoValidator {
    void validatePizzaInfo(PizzaInfoDto pizzaInfo);
}
