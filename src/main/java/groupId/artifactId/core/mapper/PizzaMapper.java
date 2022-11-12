package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.dao.entity.api.IPizza;

public class PizzaMapper {
    public PizzaMapper() {
    }

    public PizzaDtoOutput outputMapping(IPizza pizza){
        return new PizzaDtoOutput(pizza.getId(),pizza.getCompletedOrderId(), pizza.getName(), pizza.getSize(),
                pizza.getCreationDate(), pizza.getVersion());
    }
}
