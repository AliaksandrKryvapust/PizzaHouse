package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MenuItemValidatorTest {
    @InjectMocks
    private MenuItemValidator menuItemValidator;

    @Test()
    void validatePreconditionOne() {
        // preconditions
        final long id = 1L;
        final String messageFirstArg = "MenuItem`s price is not valid";
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(null, id, id);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuItemValidator.validate(menuDtoInput));

        // assert
        Assertions.assertEquals(messageFirstArg, exception.getMessage());
    }

    @Test()
    void validatePreconditionTwo() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String messageSecondArg = "MenuItem`s pizza info id is not valid";
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(price, null, id);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuItemValidator.validate(menuDtoInput));

        // assert
        Assertions.assertEquals(messageSecondArg, exception.getMessage());
    }

    @Test()
    void validatePreconditionThree() {
        // preconditions
        final long id = 1L;
        final double price = 20.0;
        final String messageThirdArg = "MenuItem`s menu id price is not valid";
        final MenuItemDtoInput menuDtoInput = new MenuItemDtoInput(price, id, null);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuItemValidator.validate(menuDtoInput));

        // assert
        Assertions.assertEquals(messageThirdArg, exception.getMessage());
    }
}