package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;

public interface IOrderService extends IService<TicketDtoCrudOutput, OrderDtoInput>, IServiceDelete {
    TicketDtoOutput getAllData(Long id);

    Boolean isItemIdValid(Long id);

    Boolean isOrderIdValid(Long id);

    Boolean isTicketIdValid(Long id);
}
