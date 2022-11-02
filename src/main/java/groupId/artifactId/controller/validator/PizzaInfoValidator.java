package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;

public class PizzaInfoValidator implements IPizzaInfoValidator {
    public PizzaInfoValidator() {
    }

    @Override
    public void validate(PizzaInfoDtoInput pizzaInfo) {
        if (pizzaInfo.getName() == null || pizzaInfo.getName().isBlank()) {
            throw new IllegalArgumentException("Pizza`s name is not valid");
        }
        if (pizzaInfo.getDescription() == null || pizzaInfo.getDescription().isBlank()) {
            throw new IllegalArgumentException("Pizza`s description is not valid");
        }
        if (pizzaInfo.getSize() <= 0) {
            throw new IllegalArgumentException("Pizza`s size is not valid");
        }
    }
}
