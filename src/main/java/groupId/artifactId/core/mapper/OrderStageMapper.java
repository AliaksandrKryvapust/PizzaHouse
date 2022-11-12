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
        return new OrderStageDtoOutput(orderStage.getId(), orderStage.getOrderDataId(), orderStage.getDescription(),
                orderStage.getCreationDate(), orderStage.getVersion());
    }
}
