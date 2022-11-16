package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    void validate() {
        // preconditions
        final String name = "";
        final boolean enable = false;
        final MenuDtoInput menuDtoInput = MenuDtoInput.builder().name(name).enable(enable).build();

        //test
        assertThrows(IllegalArgumentException.class, () -> menuValidator.validate(menuDtoInput));
    }
}