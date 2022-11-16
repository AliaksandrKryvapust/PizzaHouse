package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class PizzaInfoMapper {
    public PizzaInfoMapper() {
    }

    public IPizzaInfo inputMapping(PizzaInfoDtoInput pizzaInfoDtoInput) {
        return new PizzaInfo(pizzaInfoDtoInput.getName(), pizzaInfoDtoInput.getDescription(),
                pizzaInfoDtoInput.getSize());
    }

    public PizzaInfoDtoOutput outputMapping(IPizzaInfo pizzaInfo) {
        return PizzaInfoDtoOutput.builder()
                .id(pizzaInfo.getId())
                .name(pizzaInfo.getName())
                .description(pizzaInfo.getDescription())
                .size(pizzaInfo.getSize())
                .createdAt(pizzaInfo.getCreationDate())
                .version(pizzaInfo.getVersion()).build();
    }
}
