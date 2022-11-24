package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class MenuItemValidatorTest {
    @InjectMocks
    private MenuItemValidator menuItemValidator;
    @Mock
    private PizzaInfoValidator pizzaInfoValidator;

    @Test()
    void validatePreconditionOne() {
        // preconditions
        final long id = 1L;
        final double price = -20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final String messageFirstArg = "MenuItem`s price is not valid";
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).menuId(id)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        doNothing().when(pizzaInfoValidator).validate(any(PizzaInfoDtoInput.class));

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuItemValidator.validate(menuDtoInput));

        // assert
        Assertions.assertEquals(messageFirstArg, exception.getMessage());
    }

    @Test()
    void validatePreconditionThree() {
        // preconditions
        final double price = 20.0;
        final String pizzaName = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final long menuItemId = -1L;
        final String messageThirdArg = "MenuItem`s menu id price is not valid";
        final PizzaInfoDtoInput pizzaInfoDtoInput = PizzaInfoDtoInput.builder().name(pizzaName).description(description)
                .size(size).build();
        final MenuItemDtoInput menuDtoInput = MenuItemDtoInput.builder().price(price).menuId(menuItemId)
                .pizzaInfoDtoInput(pizzaInfoDtoInput).build();
        doNothing().when(pizzaInfoValidator).validate(any(PizzaInfoDtoInput.class));

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> menuItemValidator.validate(menuDtoInput));

        // assert
        Assertions.assertEquals(messageThirdArg, exception.getMessage());
    }
}