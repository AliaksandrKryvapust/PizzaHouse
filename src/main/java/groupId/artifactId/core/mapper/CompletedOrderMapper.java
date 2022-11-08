package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletedOrderMapper {

    public static CompletedOrderDtoOutput completedOrderOutputMapping(ICompletedOrder completedOrder) {
        List<PizzaDtoOutput> temp = new ArrayList<>();
        if (completedOrder.getItems() != null || completedOrder.getTicket() != null) {
            for (IPizza pizza : completedOrder.getItems()) {
                PizzaDtoOutput output = PizzaMapper.pizzaOutputMapper(pizza);
                temp.add(output);
            }
            TicketDtoOutPut ticketDtoOutPut = TicketMapper.ticketOutputMapping(completedOrder.getTicket());
            return new CompletedOrderDtoOutput(ticketDtoOutPut, temp, completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        } else {
            return new CompletedOrderDtoOutput(new TicketDtoOutPut(), Collections.singletonList(new PizzaDtoOutput()),
                    completedOrder.getId(), completedOrder.getTicketId(),
                    completedOrder.getCreationDate(), completedOrder.getVersion());
        }

    }
}
