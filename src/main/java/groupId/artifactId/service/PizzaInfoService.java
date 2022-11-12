package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.service.api.IPizzaInfoService;

import java.util.ArrayList;
import java.util.List;

public class PizzaInfoService implements IPizzaInfoService {

    private final IPizzaInfoDao dao;

    private final PizzaInfoMapper pizzaInfoMapper;

    public PizzaInfoService(IPizzaInfoDao dao, PizzaInfoMapper pizzaInfoMapper) {
        this.dao = dao;
        this.pizzaInfoMapper = pizzaInfoMapper;
    }

    @Override
    public PizzaInfoDtoOutput save(PizzaInfoDtoInput pizzaInfoDtoInput) {
        IPizzaInfo pizzaInfo = this.dao.save(pizzaInfoMapper.pizzaInfoInputMapping(pizzaInfoDtoInput));
        return pizzaInfoMapper.pizzaInfoOutputMapping(pizzaInfo);
    }

    @Override
    public List<PizzaInfoDtoOutput> get() {
        List<PizzaInfoDtoOutput> temp = new ArrayList<>();
        for (IPizzaInfo pizzaInfo : this.dao.get()) {
            PizzaInfoDtoOutput pizzaInfoDtoOutput = pizzaInfoMapper.pizzaInfoOutputMapping(pizzaInfo);
            temp.add(pizzaInfoDtoOutput);
        }
        return temp;
    }

    @Override
    public PizzaInfoDtoOutput get(Long id) {
        return pizzaInfoMapper.pizzaInfoOutputMapping(this.dao.get(id));
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.exist(id);
    }

    @Override
    public Boolean exist(String name) {
        return this.dao.doesPizzaExist(name);
    }

    @Override
    public PizzaInfoDtoOutput update(PizzaInfoDtoInput pizzaInfoDtoInput, String id, String version) {
        IPizzaInfo pizzaInfo = this.dao.update(pizzaInfoMapper.pizzaInfoInputMapping(pizzaInfoDtoInput),
                Long.valueOf(id), Integer.valueOf(version));
        return pizzaInfoMapper.pizzaInfoOutputMapping(pizzaInfo);
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }
}
