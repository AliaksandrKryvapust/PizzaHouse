package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;

public interface IOrderService extends IService<TicketDtoOutput, OrderDtoInput>, IServiceDelete {
    TicketDtoOutput getAllData(Long id);

    Boolean isItemIdValid(Long id);

    Boolean isOrderIdValid(Long id);

    Boolean isTicketIdValid(Long id);
}
