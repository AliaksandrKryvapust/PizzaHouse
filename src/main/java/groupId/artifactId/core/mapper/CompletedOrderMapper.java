package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.dao.entity.api.ITicket;

import java.util.List;
import java.util.stream.Collectors;

public class CompletedOrderMapper {

    private final TicketMapper ticketMapper;
    private final PizzaMapper pizzaMapper;

    public CompletedOrderMapper(TicketMapper ticketMapper, PizzaMapper pizzaMapper) {
        this.ticketMapper = ticketMapper;
        this.pizzaMapper = pizzaMapper;
    }

    public ICompletedOrder inputMapping(IOrderData orderData) {
        ITicket ticket = orderData.getTicket();
        List<IPizza> temp = ticket.getOrder().getSelectedItems().stream().map((i) -> Pizza.builder()
                        .name(i.getMenuItem().getPizzaInfo().getName()).size(i.getMenuItem().getPizzaInfo().getSize()).build())
                .collect(Collectors.toList());
        return CompletedOrder.builder().ticket(ticket).items(temp).build();
    }

    public CompletedOrderDtoCrudOutput outputCrudMapping(ICompletedOrder completedOrder) {
        return CompletedOrderDtoCrudOutput.builder()
                .id(completedOrder.getId())
                .ticketId(completedOrder.getTicket().getId())
                .createdAt(completedOrder.getCreationDate())
                .build();
    }

    public CompletedOrderDtoOutput outputMapping(ICompletedOrder completedOrder) {
        List<PizzaDtoOutput> temp = completedOrder.getItems().stream().map(pizzaMapper::outputMapping).collect(Collectors.toList());
        TicketDtoOutput ticketDtoOutPut = ticketMapper.outputMapping(completedOrder.getTicket());
        return CompletedOrderDtoOutput.builder()
                .ticket(ticketDtoOutPut)
                .items(temp)
                .id(completedOrder.getId())
                .createdAt(completedOrder.getCreationDate())
                .build();
    }
}
