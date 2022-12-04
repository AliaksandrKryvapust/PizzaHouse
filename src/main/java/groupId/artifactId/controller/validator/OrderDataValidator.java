package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import org.springframework.stereotype.Component;

@Component
public class OrderDataValidator implements IOrderDataValidator {

    @Override
    public void validate(OrderDataDtoInput orderDataDtoInput) {
        if (orderDataDtoInput.getTicketId() <= 0) {
            throw new IllegalArgumentException("None of Ticket id in OrderDataDtoInput have been sent as an input");
        }
        if (orderDataDtoInput.getDescription().isBlank()) {
            throw new IllegalArgumentException("None of Order Stage description have been sent as an input");
        }
    }
}
