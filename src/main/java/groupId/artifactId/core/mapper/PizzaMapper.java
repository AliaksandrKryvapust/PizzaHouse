package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.PizzaDtoOutput;
import groupId.artifactId.dao.entity.api.IPizza;

public class PizzaMapper {
    public PizzaMapper() {
    }

    public PizzaDtoOutput outputMapping(IPizza pizza){
        return PizzaDtoOutput.builder()
                .id(pizza.getId())
                .name(pizza.getName())
                .size(pizza.getSize())
                .build();
    }
}
