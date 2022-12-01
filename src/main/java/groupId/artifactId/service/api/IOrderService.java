package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.dao.entity.api.ITicket;

public interface IOrderService extends IService<TicketDtoCrudOutput, OrderDtoInput> {
    TicketDtoOutput getAllData(Long id);
    ITicket getRow(Long id);
}
