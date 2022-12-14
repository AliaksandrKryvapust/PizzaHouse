package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrderDataMapper {
    private final OrderStageMapper orderStageMapper;
    private final TicketMapper ticketMapper;

    @Autowired
    public OrderDataMapper(OrderStageMapper orderStageMapper, TicketMapper ticketMapper) {
        this.orderStageMapper = orderStageMapper;
        this.ticketMapper = ticketMapper;
    }

    public OrderDataDtoCrudOutput outputCrudMapping(IOrderData orderData) {
        return OrderDataDtoCrudOutput.builder()
                .id(orderData.getId())
                .ticketId(orderData.getTicket().getId())
                .done(orderData.getDone())
                .createdAt(orderData.getCreationDate())
                .build();
    }

    public OrderDataDtoOutput outputMapping(IOrderData orderData) {
        List<OrderStageDtoOutput> stageDtoOutputs = new ArrayList<>();
        for (IOrderStage stage : orderData.getOrderHistory()) {
            OrderStageDtoOutput output = orderStageMapper.outputMapping(stage);
            stageDtoOutputs.add(output);
        }
        TicketDtoOutput ticketDtoOutPut = ticketMapper.outputMapping(orderData.getTicket());
        return OrderDataDtoOutput.builder()
                .ticket(ticketDtoOutPut)
                .orderHistory(stageDtoOutputs)
                .id(orderData.getId())
                .done(orderData.getDone())
                .createdAt(orderData.getCreationDate())
                .build();
    }
}
