package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDataMapper {
    private final OrderStageMapper orderStageMapper;
    private final TicketMapper ticketMapper;

    public OrderDataMapper(OrderStageMapper orderStageMapper, TicketMapper ticketMapper) {
        this.orderStageMapper = orderStageMapper;
        this.ticketMapper = ticketMapper;
    }

    public IOrderData inputMapping(OrderDataDtoInput dtoInput) {
        List<IOrderStage> orderStage = Collections.singletonList(orderStageMapper.inputMapping(dtoInput.getDescription()));
        return new OrderData(orderStage, dtoInput.getTicketId(), dtoInput.getDone());
    }

    public OrderDataDtoCrudOutput outputCrudMapping(IOrderData orderData) {
        return OrderDataDtoCrudOutput.builder()
                .id(orderData.getId())
                .ticketId(orderData.getTicketId())
                .done(orderData.isDone())
                .createdAt(orderData.getCreationDate())
                .version(orderData.getVersion()).build();
    }

    public OrderDataDtoOutput outputMapping(IOrderData orderData) {
        List<OrderStageDtoOutput> stageDtoOutputs = new ArrayList<>();
        for (IOrderStage stage : orderData.getOrderHistory()) {
            OrderStageDtoOutput output = orderStageMapper.outputMapping(stage);
            stageDtoOutputs.add(output);
        }
        TicketDtoOutput ticketDtoOutPut = ticketMapper.outputMapping(orderData.getTicket());
        return new OrderDataDtoOutput(ticketDtoOutPut, stageDtoOutputs, orderData.getId(), orderData.getTicketId(),
                orderData.isDone(), orderData.getCreationDate(), orderData.getVersion());
    }
}
