package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.dao.entity.api.ITicket;

public class TicketMapper {
    private final OrderMapper orderMapper;

    public TicketMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public TicketDtoOutput ticketOutputMapping(ITicket ticket) {
        if (ticket.getOrder()==null){
            return new TicketDtoOutput(new OrderDtoOutput(), ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                    ticket.getVersion());
        } else {
            OrderDtoOutput orderDtoOutput = orderMapper.orderOutputMapping(ticket.getOrder());
            return new TicketDtoOutput(orderDtoOutput, ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                    ticket.getVersion());
        }

    }
}
