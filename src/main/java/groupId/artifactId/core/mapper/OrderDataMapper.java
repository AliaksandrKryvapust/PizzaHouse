package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDataMapper {
    public static IOrderData orderStageInputMapping(OrderDataDtoInput dtoInput) {
        List<IOrderStage> orderStage = Collections.singletonList(OrderStageMapper.orderStageInputMapping(dtoInput.getDescription()));
        return new OrderData(orderStage, dtoInput.getTicketId(), dtoInput.getDone());
    }

    public static OrderDataDtoOutput orderStageOutputMapping(IOrderData orderData) {
        List<OrderStageDtoOutput> stageDtoOutputs = new ArrayList<>();
        for (IOrderStage stage : orderData.getOrderHistory()) {
            OrderStageDtoOutput output = OrderStageMapper.orderStageOutputMapping(stage);
            stageDtoOutputs.add(output);
        }
        if (orderData.getTicket() == null) {
            return new OrderDataDtoOutput(new TicketDtoOutPut(), stageDtoOutputs, orderData.getId(), orderData.getTicketId(),
                    orderData.isDone(), orderData.getCreationDate(), orderData.getVersion());
        } else {
            TicketDtoOutPut ticketDtoOutPut = TicketMapper.ticketOutputMapping(orderData.getTicket());
            return new OrderDataDtoOutput(ticketDtoOutPut, stageDtoOutputs, orderData.getId(), orderData.getTicketId(),
                    orderData.isDone(), orderData.getCreationDate(), orderData.getVersion());
        }
    }
}
