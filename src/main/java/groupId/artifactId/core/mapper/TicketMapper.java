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
        return TicketDtoCrudOutput.builder()
                .id(ticket.getId())
                .orderId(ticket.getOrderId())
                .createAt(ticket.getCreateAt())
                .version(ticket.getVersion()).build();
    }

    public TicketDtoOutput outputMapping(ITicket ticket) {
        OrderDtoOutput orderDtoOutput = orderMapper.outputMapping(ticket.getOrder());
        return TicketDtoOutput.builder()
                .order(orderDtoOutput)
                .id(ticket.getId())
                .orderId(ticket.getOrderId())
                .createdAt(ticket.getCreateAt())
                .version(ticket.getVersion()).build();
    }
}
