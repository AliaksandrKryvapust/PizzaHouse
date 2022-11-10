package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import org.junit.jupiter.api.Assertions;
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
    void validatePreconditionOne() {
        // preconditions
        final boolean enable = false;
        final String messageExpected = "Menu`s name is not valid";
        final MenuDtoInput menuInput = new MenuDtoInput(null, enable);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuValidator.validate(menuInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionTwo() {
        // preconditions
        final String name = "Optional Menu";
        final String messageExpected = "Menu`s enable status is not valid";
        final MenuDtoInput menuInput = new MenuDtoInput(name, null);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuValidator.validate(menuInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }
}