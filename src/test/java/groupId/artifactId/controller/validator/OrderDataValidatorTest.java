package groupId.artifactId.controller.validator;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderDataValidatorTest {
    @InjectMocks
    private OrderDataValidator orderDataValidator;

    @Test
    void validatePreconditionOne() {
        // preconditions
        final boolean done = false;
        final String description = "Order accepted";
        final String messageExpected = "None of Ticket id in OrderDataDtoInput have been sent as an input";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(null, done, description);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderDataValidator.validate(orderDataDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionTwo() {
        // preconditions
        final long id = 1L;
        final String description = "Order accepted";
        final String messageExpected = "None of OrderData done status have been sent as an input";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, null, description);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderDataValidator.validate(orderDataDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }

    @Test
    void validatePreconditionThree() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final String messageExpected = "None of Order Stage description have been sent as an input";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, null);

        //test
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderDataValidator.validate(orderDataDtoInput));

        // assert
        Assertions.assertEquals(messageExpected, exception.getMessage());
    }
}