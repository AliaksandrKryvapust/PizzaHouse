package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    void validatePreconditionOne() {
        // preconditions
        final int count = 5;
        final String messageExpected = "Menu item id in Order is not valid";
        final OrderDtoInput orderDtoInput = new OrderDtoInput(Collections.singletonList(SelectedItemDtoInput.builder()
                .menuItemId(0L).count(count).build()));

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderValidator.validate(orderDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionTwo() {
        // preconditions
        final long id = 1L;
        final String messageExpected = "Menu item count in Order is not valid";
        final OrderDtoInput orderDtoInput = new OrderDtoInput(Collections.singletonList(SelectedItemDtoInput.builder()
                .menuItemId(id).count(0).build()));

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderValidator.validate(orderDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }
}