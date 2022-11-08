package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.dao.entity.api.ICompletedOrder;

public interface ICompletedOrderService extends IService<CompletedOrderDtoOutput, ICompletedOrder> {
    CompletedOrderDtoOutput getAllData(Long id);

    Boolean isOrderIdValid(Long id);

    Boolean isPizzaIdValid(Long id);
}
