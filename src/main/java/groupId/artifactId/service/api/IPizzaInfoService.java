package groupId.artifactId.service.api;

import groupId.artifactId.core.dto.input.PizzaInfoDto;
import groupId.artifactId.dao.entity.api.IPizzaInfo;

import java.util.List;

public interface IPizzaInfoService {
    void save(PizzaInfoDto pizzaInfoDto);
    List<IPizzaInfo> get();
    IPizzaInfo get(Long id);
    Boolean isIdValid(Long id);
    void update(PizzaInfoDto pizzaInfoDto, String id, String version);
    void delete(String id, String version);
}
