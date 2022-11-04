package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;

public interface IOrderService extends IService<TicketDtoOutPut, OrderDtoInput> {
    TicketDtoOutPut getAllData(Long id);

    Boolean isItemIdValid(Long id);

    Boolean isOrderIdValid(Long id);

    Boolean isTicketIdValid(Long id);
}
