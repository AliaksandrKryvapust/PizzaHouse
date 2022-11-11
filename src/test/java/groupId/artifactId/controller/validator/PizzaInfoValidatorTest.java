package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PizzaInfoValidatorTest {
    @InjectMocks
    private PizzaInfoValidator pizzaInfoValidator;

    @Test
    void validatePreconditionOne() {
        // preconditions
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final String messageExpected = "Pizza`s name is not valid";
        final PizzaInfoDtoInput pizzaInfoDtoInput = new PizzaInfoDtoInput(null, description, size);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pizzaInfoValidator.validate(pizzaInfoDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionTwo() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final int size = 32;
        final String messageExpected = "Pizza`s description is not valid";
        final PizzaInfoDtoInput pizzaInfoDtoInput = new PizzaInfoDtoInput(name, null, size);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pizzaInfoValidator.validate(pizzaInfoDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionThree() {
        // preconditions
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final String messageExpected = "Pizza`s size is not valid";
        final PizzaInfoDtoInput pizzaInfoDtoInput = new PizzaInfoDtoInput(name, description, null);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pizzaInfoValidator.validate(pizzaInfoDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }
}