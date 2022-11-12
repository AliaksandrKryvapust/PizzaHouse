package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletedOrderMapper {
    public CompletedOrderMapper() {
    }

    public ICompletedOrder completedOrderInputMapping(IOrderData orderData) {
        ITicket ticket = orderData.getTicket();
        List<IPizza> temp = new ArrayList<>();
        for (ISelectedItem selectedItem : ticket.getOrder().getSelectedItems()) {
            IPizza pizza = new Pizza(selectedItem.getItem().getInfo().getName(), selectedItem.getItem().getInfo().getSize());
            temp.add(pizza);
        }
        return new CompletedOrder(ticket, temp, orderData.getTicketId());
    }

    public CompletedOrderDtoOutput completedOrderOutputMapping(ICompletedOrder completedOrder) {
        List<PizzaDtoOutput> temp = new ArrayList<>();
        if (completedOrder.getItems() != null && completedOrder.getTicket() != null) {
            for (IPizza pizza : completedOrder.getItems()) {
                PizzaDtoOutput output = PizzaMapper.pizzaOutputMapper(pizza);
                temp.add(output);
            }
            TicketDtoOutPut ticketDtoOutPut = TicketMapper.ticketOutputMapping(completedOrder.getTicket());
            return new CompletedOrderDtoOutput(ticketDtoOutPut, temp, completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        } else if (completedOrder.getItems() != null) {
            for (IPizza pizza : completedOrder.getItems()) {
                PizzaDtoOutput output = PizzaMapper.pizzaOutputMapper(pizza);
                temp.add(output);
            }
            return new CompletedOrderDtoOutput(new TicketDtoOutPut(), temp, completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        } else {
            return new CompletedOrderDtoOutput(new TicketDtoOutPut(), Collections.singletonList(new PizzaDtoOutput()),
                    completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        }
    }
}
