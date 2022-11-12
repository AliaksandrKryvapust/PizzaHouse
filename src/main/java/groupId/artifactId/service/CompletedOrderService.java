package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.api.IPizzaDao;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.Pizza;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
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
        return completedOrderMapper.outputMapping(this.completedOrderDao.getAllData(id));
    }

    @Override
    public ICompletedOrder getAllDataRow(Long id) {
        return this.completedOrderDao.getAllData(id);
    }

    @Override
    public Boolean isOrderIdValid(Long id) {
        return this.completedOrderDao.exist(id);
    }

    @Override
    public Boolean isPizzaIdValid(Long id) {
        return this.pizzaDao.exist(id);
    }

    @Override
    public CompletedOrderDtoCrudOutput save(ICompletedOrder type) {
        ICompletedOrder completedOrder = this.completedOrderDao.save(type);
        List<IPizza> pizzas = new ArrayList<>();// completedOrderId needed
        for (IPizza pizza : type.getItems()) {
            IPizza output = this.pizzaDao.save(new Pizza(completedOrder.getId(), pizza.getName(), pizza.getSize()));
            pizzas.add(output);
        }
        return completedOrderMapper.outputCrudMapping(new CompletedOrder(completedOrder.getTicket(), pizzas,
                completedOrder.getId(), completedOrder.getTicketId()));
    }

    @Override
    public List<CompletedOrderDtoCrudOutput> get() {
        List<CompletedOrderDtoCrudOutput> outputs = new ArrayList<>();
        for (ICompletedOrder order : this.completedOrderDao.get()) {
            CompletedOrderDtoCrudOutput output = completedOrderMapper.outputCrudMapping(order);
            outputs.add(output);
        }
        return outputs;
    }

    @Override
    public CompletedOrderDtoCrudOutput get(Long id) {
        return completedOrderMapper.outputCrudMapping(this.completedOrderDao.get(id));
    }
}
