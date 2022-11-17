package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.dao.entity.api.ICompletedOrder;

public interface ICompletedOrderService extends IService<CompletedOrderDtoCrudOutput, ICompletedOrder> {
    CompletedOrderDtoOutput getAllData(Long id);
    ICompletedOrder getAllDataRow(Long id);
    Boolean isPizzaIdValid(Long id);
}
