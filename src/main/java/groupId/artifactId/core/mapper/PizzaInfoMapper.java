package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

public class PizzaInfoMapper {
    public PizzaInfoMapper() {
    }

    public IPizzaInfo pizzaInfoInputMapping(PizzaInfoDtoInput pizzaInfoDtoInput) {
        return new PizzaInfo(pizzaInfoDtoInput.getName(), pizzaInfoDtoInput.getDescription(),
                pizzaInfoDtoInput.getSize());
    }

    public PizzaInfoDtoOutput pizzaInfoOutputMapping(IPizzaInfo pizzaInfo) {
        return new PizzaInfoDtoOutput(pizzaInfo.getId(), pizzaInfo.getName(), pizzaInfo.getDescription(),
                pizzaInfo.getSize(), pizzaInfo.getCreationDate(), pizzaInfo.getVersion());
    }
}
