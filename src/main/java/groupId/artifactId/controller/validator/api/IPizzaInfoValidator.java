package groupId.artifactId.controller.validator.api;

import groupId.artifactId.core.dto.PizzaInfoDto;

import java.util.List;

public interface IPizzaInfoValidator {
    void validatePizzaInfo(PizzaInfoDto pizzaInfo);
}
