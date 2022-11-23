package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.api.IPizzaDao;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.ICompletedOrderService;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrderService implements ICompletedOrderService {
    private final ICompletedOrderDao completedOrderDao;
    private final IPizzaDao pizzaDao;

    private final CompletedOrderMapper completedOrderMapper;


    public CompletedOrderService(ICompletedOrderDao completedOrderDao, IPizzaDao pizzaDao, CompletedOrderMapper completedOrderMapper) {
        this.completedOrderDao = completedOrderDao;
        this.pizzaDao = pizzaDao;
        this.completedOrderMapper = completedOrderMapper;
    }

    @Override
    public CompletedOrderDtoOutput getAllData(Long id) {
        try {
            return completedOrderMapper.outputMapping(this.completedOrderDao.getAllData(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAllData at Completed order Service by id" + id, e);
        }
    }

    @Override
    public ICompletedOrder getAllDataRow(Long id) {
        try {
            return this.completedOrderDao.getAllData(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAllDataRow at Completed order Service by id" + id, e);
        }
    }

    @Override
    public Boolean isPizzaIdValid(Long id) {
        try {
            return this.pizzaDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check pizza state at Completed order Service by id" + id, e);
        }
    }

    @Override
    public CompletedOrderDtoCrudOutput save(ICompletedOrder type) {
        try {
            ICompletedOrder completedOrder = this.completedOrderDao.save(type, EntityManagerFactoryHibernate.getEntityManager());
            List<IPizza> pizzas = new ArrayList<>();// completedOrderId needed
            for (IPizza pizza : type.getItems()) {
                IPizza output = this.pizzaDao.save(new Pizza(completedOrder.getId(), pizza.getName(), pizza.getSize()), EntityManagerFactoryHibernate.getEntityManager());
                pizzas.add(output);
            }
            return completedOrderMapper.outputCrudMapping(new CompletedOrder(completedOrder.getTicket(), pizzas,
                    completedOrder.getId(), completedOrder.getTicketId()));
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
