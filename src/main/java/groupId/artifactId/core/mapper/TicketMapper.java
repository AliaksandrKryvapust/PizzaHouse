package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.dao.entity.api.ITicket;

public class TicketMapper {
    public static TicketDtoOutPut ticketOutputMapping(ITicket ticket) {
        return new TicketDtoOutPut(new OrderDtoOutput(), ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                ticket.getVersion());
    }
    public static TicketDtoOutPut ticketOutputForAllDataMapping(ITicket ticket) {
        OrderDtoOutput orderDtoOutput = OrderMapper.orderOutputMapping(ticket.getOrder());
        return new TicketDtoOutPut(orderDtoOutput, ticket.getId(), ticket.getOrderId(), ticket.getCreateAt(),
                ticket.getVersion());
    }
}
