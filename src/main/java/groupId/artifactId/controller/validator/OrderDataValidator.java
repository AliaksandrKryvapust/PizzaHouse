package groupId.artifactId.controller.validator;

import groupId.artifactId.controller.validator.api.IOrderDataValidator;
import groupId.artifactId.core.dto.input.OrderDataDtoInput;

public class OrderDataValidator implements IOrderDataValidator {

    public OrderDataValidator() {
    }

    @Override
    public void validate(OrderDataDtoInput orderDataDtoInput) {
        if (orderDataDtoInput.getTicketId() == null || orderDataDtoInput.getTicketId() < 0) {
            throw new IllegalStateException("None of Ticket id in OrderDataDtoInput have been sent as an input");
        }
        if (orderDataDtoInput.getDescription() == null || orderDataDtoInput.getDescription().isBlank()) {
            throw new IllegalStateException("None of Order Stage description have been sent as an input");
        }
        if (orderDataDtoInput.getDone() == null) {
            throw new IllegalStateException("None of OrderData done status have been sent as an input");
        }
    }
}
