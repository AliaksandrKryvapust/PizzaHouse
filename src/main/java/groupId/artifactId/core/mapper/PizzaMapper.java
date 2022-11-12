package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.dao.entity.api.IPizza;

public class PizzaMapper {
    public PizzaDtoOutput pizzaOutputMapper(IPizza pizza){
        return new PizzaDtoOutput(pizza.getId(),pizza.getCompletedOrderId(), pizza.getName(), pizza.getSize(),
                pizza.getCreationDate(), pizza.getVersion());
    }
}
