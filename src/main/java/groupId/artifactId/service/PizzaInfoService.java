package groupId.artifactId.service;

import groupId.artifactId.controller.validator.PizzaInfoValidator;
import groupId.artifactId.core.dto.input.PizzaInfoDto;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.controller.validator.api.IPizzaInfoValidator;

import java.util.List;

public class PizzaInfoService implements IPizzaInfoService {
    private static PizzaInfoService firstInstance = null;
    private final IPizzaInfoDao dao;
    private final IPizzaInfoValidator validator;

    private PizzaInfoService() {
        this.dao = PizzaInfoDao.getInstance();
        this.validator = PizzaInfoValidator.getInstance();
    }

    public static PizzaInfoService getInstance() {
        synchronized (PizzaInfoService.class) {
            if (firstInstance == null) {
                firstInstance = new PizzaInfoService();
            }
        }
        return firstInstance;
    }

    @Override
    public void save(PizzaInfoDto pizzaInfoDto) {
        this.validator.validatePizzaInfo(pizzaInfoDto);
        this.dao.save(MenuMapper.pizzaInfoMapping(pizzaInfoDto));
    }

    @Override
    public List<IPizzaInfo> get() {
        return this.dao.get();
    }

    @Override
    public IPizzaInfo get(Long id) {
        return this.dao.get(id);
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.isIdExist(id);
    }

    @Override
    public void update(PizzaInfoDto pizzaInfoDto, String id, String version) {
        this.validator.validatePizzaInfo(pizzaInfoDto);
        this.dao.update(MenuMapper.pizzaInfoMapping(pizzaInfoDto), Long.valueOf(id), Integer.valueOf(version));
    }

    @Override
    public void delete(String id, String version) {
        this.dao.delete(Long.valueOf(id), Integer.valueOf(version), false);
    }
}
