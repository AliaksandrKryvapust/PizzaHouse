package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.api.IOrderStageDao;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.service.api.IOrderDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDataService implements IOrderDataService {
    private final IOrderDataDao orderDataDao;
    private final IOrderStageDao orderStageDao;
    private final ICompletedOrderService completedOrderService;
    private final CompletedOrderMapper completedOrderMapper;
    private final OrderDataMapper orderDataMapper;

    public OrderDataService(IOrderDataDao orderDataDao, IOrderStageDao orderStageDao, ICompletedOrderService completedOrderService,
                            CompletedOrderMapper completedOrderMapper, OrderDataMapper orderDataMapper) {
        this.orderDataDao = orderDataDao;
        this.orderStageDao = orderStageDao;
        this.completedOrderService = completedOrderService;
        this.completedOrderMapper = completedOrderMapper;
        this.orderDataMapper= orderDataMapper;
    }

    @Override
    public OrderDataDtoOutput getAllData(Long id) {
        try {
            return orderDataMapper.outputMapping(this.orderDataDao.getAllData(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Order Data at Service by id" + id, e);
        }
    }

    @Override
    public Boolean isIdValid(Long id) {
        try {
            return this.orderDataDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Order Data at Service by id " + id, e);
        }
    }

    @Override
    public Boolean isOrderStageIdValid(Long id) {
        try {
            return this.orderStageDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Order Stage at Service by id " + id, e);
        }
    }

    @Override
    public Boolean isTicketIdValid(Long id) {
        try {
            return this.orderDataDao.doesTicketExist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Ticket at Service by id " + id, e);
        }
    }

    @Override
    public Boolean exist(Long orderDataId, String description) {
        try {
            return this.orderStageDao.doesStageExist(orderDataId, description);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Order Data at Service by id " + orderDataId +
                    " and description " + description, e);
        }
    }

    @Override
    public OrderDataDtoCrudOutput save(OrderDataDtoInput type) {
        try {
            IOrderData input = orderDataMapper.inputMapping(type);
            IOrderData id;
            if (!this.orderDataDao.doesTicketExist(type.getTicketId())) {
                id = this.orderDataDao.save(input);
            } else {
                id = this.orderDataDao.getDataByTicket(type.getTicketId());
            }
            IOrderStage stage = this.orderStageDao.save(new OrderStage(id.getId(), input.getOrderHistory().get(0).getDescription()));
            return orderDataMapper.outputCrudMapping(new OrderData(Collections.singletonList(stage),
                    id.getId(), id.getTicketId(), id.isDone()));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Order Data" + type, e);
        }
    }

    @Override
    public List<OrderDataDtoCrudOutput> get() {
        try {
            List<OrderDataDtoCrudOutput> temp = new ArrayList<>();
            for (IOrderData orderData : this.orderDataDao.get()) {
                OrderDataDtoCrudOutput output = orderDataMapper.outputCrudMapping(orderData);
                temp.add(output);
            }
            return temp;
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
        } catch (Exception e) {
            throw new ServiceException("Failed to get Order Data at Service by id" + id, e);
        }
    }

    @Override
    public OrderDataDtoCrudOutput update(OrderDataDtoInput type, String id, String version) {
        try {
            IOrderData input = orderDataMapper.inputMapping(type);
            IOrderData orderData;
            IOrderStage stage;
            if (input.isDone()) {
                orderData = this.orderDataDao.update(input, Long.valueOf(id), Integer.valueOf(version));
                ICompletedOrder completedOrder = completedOrderMapper.inputMapping(this.orderDataDao
                        .getAllData(type.getTicketId()));
                completedOrderService.save(completedOrder);
            } else {
                orderData = new OrderData(Long.valueOf(id), input.getTicketId(), input.isDone());
            }
            if (!this.orderStageDao.doesStageExist(orderData.getId(), input.getOrderHistory().get(0).getDescription())) {
                stage = this.orderStageDao.save(new OrderStage(Long.valueOf(id),
                        input.getOrderHistory().get(0).getDescription()));
            } else {
                stage = input.getOrderHistory().get(0);
            }
            return orderDataMapper.outputCrudMapping(new OrderData(Collections.singletonList(stage),
                    orderData.getId(), orderData.getTicketId(), orderData.isDone()));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to update Order Data " + type + "by id:" + id, e);
        }
    }
}
