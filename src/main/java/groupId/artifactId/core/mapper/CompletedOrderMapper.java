package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.*;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrderMapper {

    private final TicketMapper ticketMapper;
    private final PizzaMapper pizzaMapper;

    public CompletedOrderMapper(TicketMapper ticketMapper, PizzaMapper pizzaMapper) {
        this.ticketMapper = ticketMapper;
        this.pizzaMapper = pizzaMapper;
    }

    public ICompletedOrder inputMapping(IOrderData orderData) {
        ITicket ticket = orderData.getTicket();
        List<IPizza> temp = new ArrayList<>();
        for (ISelectedItem selectedItem : ticket.getOrder().getSelectedItems()) {
            IPizza pizza = new Pizza(selectedItem.getItem().getPizzaInfo().getName(),
                    selectedItem.getItem().getPizzaInfo().getSize());
            temp.add(pizza);
        }
        return new CompletedOrder(ticket, temp, orderData.getTicketId());
    }

    public CompletedOrderDtoCrudOutput outputCrudMapping(ICompletedOrder completedOrder) {
        return CompletedOrderDtoCrudOutput.builder()
                .id(completedOrder.getId())
                .ticketId(completedOrder.getTicketId())
                .createdAt(completedOrder.getCreationDate())
                .version(completedOrder.getVersion()).build();
    }

    public CompletedOrderDtoOutput outputMapping(ICompletedOrder completedOrder) {
        List<PizzaDtoOutput> temp = new ArrayList<>();
        for (IPizza pizza : completedOrder.getItems()) {
            PizzaDtoOutput output = pizzaMapper.outputMapping(pizza);
            temp.add(output);
        }
        TicketDtoOutput ticketDtoOutPut = ticketMapper.outputMapping(completedOrder.getTicket());
        return CompletedOrderDtoOutput.builder()
                .ticket(ticketDtoOutPut)
                .items(temp)
                .id(completedOrder.getId())
                .ticketId(completedOrder.getTicketId())
                .createdAt(completedOrder.getCreationDate())
                .version(completedOrder.getVersion()).build();
    }
}
