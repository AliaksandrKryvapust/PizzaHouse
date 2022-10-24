package groupId.artifactId.service;

import groupId.artifactId.core.dto.PizzaInfoDto;
import groupId.artifactId.dao.PizzaInfoDao;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.service.api.IPizzaInfoService;
import groupId.artifactId.service.api.IPizzaInfoValidator;

import java.sql.SQLException;
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

    }

    @Override
    public List<PizzaInfo> get() {
        return this.dao.get();
    }

    @Override
    public IPizzaInfo get(Long id) {
        try {
            return this.dao.get(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get PizzaInfo with id " + id,e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.dao.isIdExist(id);
    }

    @Override
    public void update(PizzaInfoDto pizzaInfoDto) {

    }

    @Override
    public void delete(String id, String version) {

    }
}
