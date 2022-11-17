package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.exceptions.ServiceException;
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
        try {
            IPizzaInfo pizzaInfo = this.dao.save(pizzaInfoMapper.inputMapping(pizzaInfoDtoInput));
            return pizzaInfoMapper.outputMapping(pizzaInfo);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Pizza Info" + pizzaInfoDtoInput, e);
        }
    }

    @Override
    public List<PizzaInfoDtoOutput> get() {
        try {
            List<PizzaInfoDtoOutput> temp = new ArrayList<>();
            for (IPizzaInfo pizzaInfo : this.dao.get()) {
                PizzaInfoDtoOutput pizzaInfoDtoOutput = pizzaInfoMapper.outputMapping(pizzaInfo);
                temp.add(pizzaInfoDtoOutput);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Pizza Info`s at Service", e);
        }
    }

    @Override
    public PizzaInfoDtoOutput get(Long id) {
        try {
            return pizzaInfoMapper.outputMapping(this.dao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Pizza Info at Service by id" + id, e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        try {
            return this.dao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Pizza Info at Service by id " + id, e);
        }
    }

    @Override
    public Boolean exist(String name) {
        try {
            return this.dao.doesPizzaExist(name);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Pizza Info at Service by name " + name, e);
        }
    }

    @Override
    public PizzaInfoDtoOutput update(PizzaInfoDtoInput pizzaInfoDtoInput, String id, String version) {
        if (!isIdValid(Long.valueOf(id))) {
            throw new NoContentException("Pizza Info Id is not valid");
        }
        try {
            IPizzaInfo pizzaInfo = this.dao.update(pizzaInfoMapper.inputMapping(pizzaInfoDtoInput),
                    Long.valueOf(id), Integer.valueOf(version));
            return pizzaInfoMapper.outputMapping(pizzaInfo);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Pizza Info " + pizzaInfoDtoInput + "by id:" + id, e);
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            this.dao.delete(Long.valueOf(id), Boolean.valueOf(delete));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Pizza Info with id:" + id, e);
        }
    }
}
