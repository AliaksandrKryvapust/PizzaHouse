package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.ICompletedOrderService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class CompletedOrderService implements ICompletedOrderService {
    private final ICompletedOrderDao completedOrderDao;
    private final CompletedOrderMapper completedOrderMapper;
    private final EntityManager entityManager;

    public CompletedOrderService(ICompletedOrderDao completedOrderDao,
                                 CompletedOrderMapper completedOrderMapper, EntityManager entityManager) {
        this.completedOrderDao = completedOrderDao;
        this.completedOrderMapper = completedOrderMapper;
        this.entityManager = entityManager;
    }

    @Override
    public CompletedOrderDtoOutput getAllData(Long id) {
        try {
            return completedOrderMapper.outputMapping(this.completedOrderDao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAllData at Completed order Service by id" + id, e);
        }
    }

    @Override
    public CompletedOrderDtoCrudOutput create(ICompletedOrder type, EntityManager entityTransaction) {
        try {
            ICompletedOrder completedOrder = this.completedOrderDao.save(type, entityTransaction);
            return completedOrderMapper.outputCrudMapping(completedOrder);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Completed order" + type, e);
        }
    }
    @Override
    public CompletedOrderDtoCrudOutput save(ICompletedOrder type) {
        try {
            entityManager.getTransaction().begin();
            CompletedOrderDtoCrudOutput output = this.create(type, this.entityManager);
            entityManager.getTransaction().commit();
            return output;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Completed order" + type, e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<CompletedOrderDtoCrudOutput> get() {
        try {
            List<CompletedOrderDtoCrudOutput> outputs = new ArrayList<>();
            for (ICompletedOrder order : this.completedOrderDao.get()) {
                CompletedOrderDtoCrudOutput output = completedOrderMapper.outputCrudMapping(order);
                outputs.add(output);
            }
            return outputs;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Completed order at Service", e);
        }
    }

    @Override
    public CompletedOrderDtoCrudOutput get(Long id) {
        try {
            return completedOrderMapper.outputCrudMapping(this.completedOrderDao.get(id));
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to getAllData at Completed order Service by id" + id, e);
        }
    }
}
