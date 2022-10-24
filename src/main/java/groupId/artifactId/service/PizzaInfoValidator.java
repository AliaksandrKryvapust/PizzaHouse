package groupId.artifactId.service;

import groupId.artifactId.core.dto.PizzaInfoDto;
import groupId.artifactId.service.api.IPizzaInfoValidator;

import java.util.List;

public class PizzaInfoValidator implements IPizzaInfoValidator {

    private static PizzaInfoValidator firstInstance = null;

    public static PizzaInfoValidator getInstance() {
        synchronized (PizzaInfoValidator.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoValidator();
            }
            return firstInstance;
        }
    }
    @Override
    public void validateListPizzaInfo(List<PizzaInfoDto> pizzaInfos) {

    }

    @Override
    public void validatePizzaInfo(PizzaInfoDto pizzaInfo) {

    }
}
