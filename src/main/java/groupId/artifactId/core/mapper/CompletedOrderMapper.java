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

import static java.util.Collections.singletonList;

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
            IPizza pizza = new Pizza(selectedItem.getItem().getInfo().getName(), selectedItem.getItem().getInfo().getSize());
            temp.add(pizza);
        }
        return new CompletedOrder(ticket, temp, orderData.getTicketId());
    }

    public CompletedOrderDtoCrudOutput outputCrudMapping(ICompletedOrder completedOrder) {
        return CompletedOrderDtoCrudOutput.builder().id(completedOrder.getId()).ticketId(completedOrder.getTicketId())
                .createdAt(completedOrder.getCreationDate()).version(completedOrder.getVersion()).build();
    }

    public CompletedOrderDtoOutput outputMapping(ICompletedOrder completedOrder) {
        List<PizzaDtoOutput> temp = new ArrayList<>();
        if (completedOrder.getItems() != null && completedOrder.getTicket() != null) {
            for (IPizza pizza : completedOrder.getItems()) {
                PizzaDtoOutput output = pizzaMapper.outputMapping(pizza);
                temp.add(output);
            }
            TicketDtoOutput ticketDtoOutPut = ticketMapper.outputMapping(completedOrder.getTicket());
            return new CompletedOrderDtoOutput(ticketDtoOutPut, temp, completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        } else if (completedOrder.getItems() != null) {
            for (IPizza pizza : completedOrder.getItems()) {
                PizzaDtoOutput output = pizzaMapper.outputMapping(pizza);
                temp.add(output);
            }
            return new CompletedOrderDtoOutput(new TicketDtoOutput(), temp, completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        } else {
            return new CompletedOrderDtoOutput(new TicketDtoOutput(), singletonList(new PizzaDtoOutput()),
                    completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        }
    }
}
