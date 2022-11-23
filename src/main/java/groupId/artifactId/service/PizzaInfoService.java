package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.PizzaInfoDtoInput;
import groupId.artifactId.core.dto.output.PizzaInfoDtoOutput;
import groupId.artifactId.core.mapper.PizzaInfoMapper;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IPizzaInfoDao;
import groupId.artifactId.dao.entity.api.IPizzaInfo;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IPizzaInfoService;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import java.util.ArrayList;
import java.util.List;

public class PizzaInfoService implements IPizzaInfoService {

    private final IPizzaInfoDao dao;

    private final EntityManager entityManager;

    private final PizzaInfoMapper pizzaInfoMapper;

    public PizzaInfoService(IPizzaInfoDao dao, PizzaInfoMapper pizzaInfoMapper, EntityManager entityManager) {
        this.dao = dao;
        this.pizzaInfoMapper = pizzaInfoMapper;
        this.entityManager = entityManager;
    }

    @Override
    public PizzaInfoDtoOutput save(PizzaInfoDtoInput pizzaInfoDtoInput) {
        try {
            entityManager.getTransaction().begin();
            IPizzaInfo pizzaInfo = this.dao.save(pizzaInfoMapper.inputMapping(pizzaInfoDtoInput), EntityManagerFactoryHibernate.getEntityManager());
            entityManager.getTransaction().commit();
            return pizzaInfoMapper.outputMapping(pizzaInfo);
        } catch (DaoException e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            entityManager.getTransaction().rollback();
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException("Failed to save Pizza Info at Service" + pizzaInfoDtoInput + "\tcause:"
                    + e.getMessage(), e);
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
            throw new ServiceException("Failed to get List of Pizza Info`s at Service\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public PizzaInfoDtoOutput get(Long id) {
        try {
            IPizzaInfo pizzaInfo = this.dao.get(id);
            return pizzaInfoMapper.outputMapping(pizzaInfo);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Pizza Info at Service by id" + id + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public PizzaInfoDtoOutput update(PizzaInfoDtoInput pizzaInfoDtoInput, String id, String version) {
        try {
            entityManager.getTransaction().begin();
            IPizzaInfo pizzaInfo = this.dao.update(pizzaInfoMapper.inputMapping(pizzaInfoDtoInput),
                    Long.valueOf(id), Integer.valueOf(version));
            entityManager.getTransaction().commit();
            return pizzaInfoMapper.outputMapping(pizzaInfo);
        } catch (DaoException e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException(e.getMessage(), e);
        } catch (OptimisticLockException e) {
            entityManager.getTransaction().rollback();
            throw new OptimisticLockException(e.getMessage());
        } catch (NoContentException e) {
            entityManager.getTransaction().rollback();
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException("Failed to update Pizza Info at Service " + pizzaInfoDtoInput + "by id:" + id
                    + "\tcause" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            entityManager.getTransaction().begin();
            this.dao.delete(Long.valueOf(id), Boolean.valueOf(delete));
            entityManager.getTransaction().commit();
        } catch (DaoException e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            entityManager.getTransaction().rollback();
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ServiceException("Failed to delete Pizza Info with id:" + id, e);
        }
    }
}
