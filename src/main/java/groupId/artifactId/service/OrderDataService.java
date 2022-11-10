package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.dao.api.IOrderDataDao;
import groupId.artifactId.dao.api.IOrderStageDao;
import groupId.artifactId.dao.entity.OrderData;
import groupId.artifactId.dao.entity.OrderStage;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import groupId.artifactId.service.api.ICompletedOrderService;
import groupId.artifactId.service.api.IOrderDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDataService implements IOrderDataService {
    private final IOrderDataDao orderDataDao;
    private final IOrderStageDao orderStageDao;
    private final ICompletedOrderService completedOrderService;

    public OrderDataService(IOrderDataDao orderDataDao, IOrderStageDao orderStageDao, ICompletedOrderService completedOrderService) {
        this.orderDataDao = orderDataDao;
        this.orderStageDao = orderStageDao;
        this.completedOrderService = completedOrderService;
    }

    @Override
    public OrderDataDtoOutput getAllData(Long id) {
        return OrderDataMapper.orderDataOutputMapping(this.orderDataDao.getAllData(id));
    }

    @Override
    public Boolean isIdValid(Long id) {
        return this.orderDataDao.exist(id);
    }

    @Override
    public Boolean isOrderStageIdValid(Long id) {
        return this.orderStageDao.exist(id);
    }

    @Override
    public Boolean isTicketIdValid(Long id) {
        return this.orderDataDao.doesTicketExist(id);
    }

    @Override
    public Boolean exist(Long orderDataId, String description) {
        return this.orderStageDao.doesStageExist(orderDataId, description);
    }

    @Override
    public OrderDataDtoOutput save(OrderDataDtoInput type) {
        IOrderData input = OrderDataMapper.orderDataInputMapping(type);
        IOrderData id;
        if (!this.orderDataDao.doesTicketExist(type.getTicketId())) {
            id = this.orderDataDao.save(input);
        } else {
            id = this.orderDataDao.getDataByTicket(type.getTicketId());
        }
        IOrderStage stage = this.orderStageDao.save(new OrderStage(id.getId(), input.getOrderHistory().get(0).getDescription()));
        return OrderDataMapper.orderDataOutputMapping(new OrderData(Collections.singletonList(stage),
                id.getId(), id.getTicketId(), id.isDone()));
    }

    @Override
    public List<OrderDataDtoOutput> get() {
        List<OrderDataDtoOutput> temp = new ArrayList<>();
        for (IOrderData orderData : this.orderDataDao.get()) {
            OrderDataDtoOutput output = OrderDataMapper.orderDataOutputMapping(orderData);
            temp.add(output);
        }
        return temp;
    }

    @Override
    public OrderDataDtoOutput get(Long id) {
        return OrderDataMapper.orderDataOutputMapping(this.orderDataDao.get(id));
    }

    @Override
    public OrderDataDtoOutput update(OrderDataDtoInput type, String id, String version) {
        IOrderData input = OrderDataMapper.orderDataInputMapping(type);
        IOrderData orderData;
        IOrderStage stage;
        if (input.isDone()) {
            orderData = this.orderDataDao.update(input, Long.valueOf(id), Integer.valueOf(version));
            ICompletedOrder completedOrder = CompletedOrderMapper.completedOrderInputMapping(this.orderDataDao.getAllData(type.getTicketId()));
            completedOrderService.save(completedOrder);
        } else {
            orderData = new OrderData(Long.valueOf(id),input.getTicketId(), input.isDone());
        }
        if (!this.orderStageDao.doesStageExist(orderData.getId(), input.getOrderHistory().get(0).getDescription())) {
            stage = this.orderStageDao.save(new OrderStage(Long.valueOf(id),
                    input.getOrderHistory().get(0).getDescription()));
        } else {
            stage = input.getOrderHistory().get(0);
        }
        return OrderDataMapper.orderDataOutputMapping(new OrderData(Collections.singletonList(stage),
                orderData.getId(), orderData.getTicketId(), orderData.isDone()));
    }
}
