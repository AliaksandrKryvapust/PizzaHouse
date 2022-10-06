package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.OrderDataDto;
import groupId.artifactId.core.dto.OrderStageDtoWithId;
import groupId.artifactId.storage.entity.OrderData;
import groupId.artifactId.storage.entity.OrderStage;
import groupId.artifactId.storage.entity.api.IOrderData;
import groupId.artifactId.storage.entity.api.IOrderStage;

public class OrderDataMapper {
    public static IOrderStage orderStageWithIdMapping(OrderStageDtoWithId orderStageDtoWithId){
        return new OrderStage(orderStageDtoWithId.getDescription(), orderStageDtoWithId.getTime());
    }
    public static IOrderData orderDataMapping(OrderDataDto orderDataDto){
        return new OrderData(orderDataDto.getToken(),orderDataDto.getDone());
    }
}
