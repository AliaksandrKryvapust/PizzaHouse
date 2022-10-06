package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.storage.entity.OrderStage;
import groupId.artifactId.storage.entity.api.IOrderStage;

public class OrderDataMapper {
    public static IOrderStage OrderStageWithIdMapping(OrderStageDtoWithId orderStageDtoWithId){
        return new OrderStage(orderStageDtoWithId.getDescription(), orderStageDtoWithId.getTime());
    }
}
