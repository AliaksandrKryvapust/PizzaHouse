package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.dao.entity.api.ITicket;

public class TicketMapper {
    private final OrderMapper orderMapper;

    public TicketMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public TicketDtoCrudOutput outputCrudMapping(ITicket ticket) {
        return new TicketDtoCrudOutput(ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                ticket.getVersion());
    }

    public TicketDtoOutput outputMapping(ITicket ticket) {
        if (ticket.getOrder() == null) {
            return new TicketDtoOutput(new OrderDtoOutput(), ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                    ticket.getVersion());
        } else {
            OrderDtoOutput orderDtoOutput = orderMapper.outputMapping(ticket.getOrder());
            return new TicketDtoOutput(orderDtoOutput, ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                    ticket.getVersion());
        }
    }
}
