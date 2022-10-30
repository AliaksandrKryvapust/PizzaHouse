package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.PizzaInfoDto;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;

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
    public void validatePizzaInfo(PizzaInfoDto pizzaInfo) {
        if (pizzaInfo.getName() == null || pizzaInfo.getName().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s name is not valid");
        }
        if (pizzaInfo.getDescription() == null || pizzaInfo.getDescription().isBlank()) {
            throw new IllegalArgumentException("Error code 400. Pizza`s description is not valid");
        }
        if (pizzaInfo.getSize() <= 0) {
            throw new IllegalArgumentException("Error code 400. Pizza`s size is not valid");
        }
    }
}
