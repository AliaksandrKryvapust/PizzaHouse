package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IMenuValidator;
import groupId.artifactId.core.dto.input.MenuDtoInput;

public class MenuValidator implements IMenuValidator {

    public MenuValidator() {
    }

    @Override
    public void validate(MenuDtoInput menuDtoInput) {
        if (menuDtoInput.getName() == null || menuDtoInput.getName().isBlank()) {
            throw new IllegalArgumentException("Menu`s name is not valid");
        }
        if (menuDtoInput.getEnable() == null) {
            throw new IllegalArgumentException("Menu`s enable status is not valid");
        }
    }

}
