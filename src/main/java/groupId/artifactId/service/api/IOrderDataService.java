package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;

public interface IOrderDataService extends IService<OrderDataDtoCrudOutput, OrderDataDtoInput>,
        IServiceUpdate<OrderDataDtoCrudOutput, OrderDataDtoInput> {
    OrderDataDtoOutput getAllData(Long id);

    Boolean isIdValid(Long id);

    Boolean isOrderStageIdValid(Long id);

    Boolean isTicketIdValid(Long id);

    Boolean exist(Long orderDataId, String description);
}
