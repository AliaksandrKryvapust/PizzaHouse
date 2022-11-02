package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;

public interface IPizzaInfoService extends IService<PizzaInfoDtoOutput, PizzaInfoDtoInput> {

    Boolean isIdValid(Long id);

    Boolean exist(String name);
}
