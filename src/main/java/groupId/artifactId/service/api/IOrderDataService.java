package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;

import javax.persistence.EntityManager;

public interface IOrderDataService extends IService<OrderDataDtoCrudOutput, OrderDataDtoInput> {
    OrderDataDtoOutput getAllData(Long id);
    void create(OrderDataDtoInput dtoInput, EntityManager entityManager);
}
