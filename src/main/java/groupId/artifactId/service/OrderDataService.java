package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.core.mapper.OrderStageMapper;
import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IOrderDataService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static groupId.artifactId.core.Constants.ORDER_FINISH_DESCRIPTION;
import static java.util.Collections.singletonList;

public class OrderDataService implements IOrderDataService {
    private final IOrderDataDao orderDataDao;
    private final OrderDataMapper orderDataMapper;
    private final OrderStageMapper orderStageMapper;
    private final EntityManager entityManager;

    public OrderDataService(IOrderDataDao orderDataDao, OrderDataMapper orderDataMapper,
                            OrderStageMapper orderStageMapper, EntityManager entityManager) {
        this.orderDataDao = orderDataDao;
        this.orderDataMapper = orderDataMapper;
        this.orderStageMapper = orderStageMapper;
        this.entityManager = entityManager;
    }

    @Override
    public OrderDataDtoOutput getAllData(Long id) {
        try {
            return orderDataMapper.outputMapping(this.orderDataDao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Order Data at Service by Ticket id" + id, e);
        }
    }

    @Override
    public void create(OrderDataDtoInput dtoInput, EntityManager entityTransaction) {
        try {
            IOrderStage inputOrderStages = this.orderStageMapper.inputMapping(dtoInput.getDescription());
            OrderData orderData = OrderData.builder().orderHistory(singletonList(inputOrderStages))
                    .ticket(dtoInput.getTicket()).build();
            orderDataDao.save(orderData, entityTransaction);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to save Order Data" + dtoInput, e);
        }
    }

    @Override
    public OrderDataDtoCrudOutput save(OrderDataDtoInput dtoInput) {
        try {
            IOrderStage inputOrderStages = this.orderStageMapper.inputMapping(dtoInput.getDescription());
            entityManager.getTransaction().begin();
            OrderData orderData = (OrderData) this.orderDataDao.getOptional(dtoInput.getTicketId(),this.entityManager);
            orderData.getOrderHistory().add(inputOrderStages);
            orderData.setDone(inputOrderStages.getDescription().equals(ORDER_FINISH_DESCRIPTION));
            IOrderData orderDataOutput = this.orderDataDao.update(orderData, this.entityManager);
//            if (inputOrderStages.getDescription().equals(ORDER_FINISH_DESCRIPTION)){
//                this.completedOrderService.save();
//            }
            entityManager.getTransaction().commit();
            return orderDataMapper.outputCrudMapping(orderDataOutput);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to save Order Data" + dtoInput, e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<OrderDataDtoCrudOutput> get() {
        try {
            return this.orderDataDao.get().stream().map(orderDataMapper::outputCrudMapping).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get Order Data at Service", e);
        }
    }

    @Override
    public OrderDataDtoCrudOutput get(Long id) {
        try {
            return orderDataMapper.outputCrudMapping(this.orderDataDao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Order Data at Service by Ticket id" + id, e);
        }
    }
}
