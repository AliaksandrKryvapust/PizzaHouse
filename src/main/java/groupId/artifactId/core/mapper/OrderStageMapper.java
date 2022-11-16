package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.IOrderStage;

public class OrderStageMapper {
    public OrderStageMapper() {
    }

    public IOrderStage inputMapping(String description) {
        return new OrderStage(description);
    }

    public OrderStageDtoOutput outputMapping(IOrderStage orderStage) {
        return OrderStageDtoOutput.builder()
                .id(orderStage.getId())
                .orderDataId(orderStage.getOrderDataId())
                .description(orderStage.getDescription())
                .createdAt(orderStage.getCreationDate())
                .version(orderStage.getVersion()).build();
    }
}
