package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.PizzaInfoDto;

import java.util.List;

public interface IPizzaInfoValidator {
    void validateListPizzaInfo(List<PizzaInfoDto> pizzaInfos);
    void validatePizzaInfo(PizzaInfoDto pizzaInfo);
}
