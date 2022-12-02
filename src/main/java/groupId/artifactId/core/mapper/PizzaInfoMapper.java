package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class PizzaInfoMapper {
    public PizzaInfoMapper() {
    }

    public IPizzaInfo inputMapping(PizzaInfoDtoInput pizzaInfoDtoInput) {
        return PizzaInfo.builder()
                .name(pizzaInfoDtoInput.getName())
                .description(pizzaInfoDtoInput.getDescription())
                .size(pizzaInfoDtoInput.getSize()).build();
    }

    public PizzaInfoDtoOutput outputMapping(IPizzaInfo pizzaInfo) {
        return PizzaInfoDtoOutput.builder()
                .name(pizzaInfo.getName())
                .description(pizzaInfo.getDescription())
                .size(pizzaInfo.getSize())
                .build();
    }
}
