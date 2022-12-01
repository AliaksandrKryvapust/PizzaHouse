package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.IOrderStage;

public class OrderStageMapper {
    public OrderStageMapper() {
    }

    public IOrderStage inputMapping(String description) {
        return OrderStage.builder().description(description).build();
    }

    public OrderStageDtoOutput outputMapping(IOrderStage orderStage) {
        return OrderStageDtoOutput.builder()
                .id(orderStage.getId())
                .description(orderStage.getDescription())
                .createdAt(orderStage.getCreationDate())
                .build();
    }
}
