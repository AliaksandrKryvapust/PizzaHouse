package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.CompletedOrderDtoOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.api.ICompletedOrderDao;
import groupId.artifactId.dao.api.IPizzaDao;
import groupId.artifactId.dao.entity.CompletedOrder;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
import groupId.artifactId.service.api.ICompletedOrderService;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrderService implements ICompletedOrderService {
    private final ICompletedOrderDao completedOrderDao;
    private final IPizzaDao pizzaDao;

    public CompletedOrderService(ICompletedOrderDao completedOrderDao, IPizzaDao pizzaDao) {
        this.completedOrderDao = completedOrderDao;
        this.pizzaDao = pizzaDao;
    }

    @Override
    public CompletedOrderDtoOutput getAllData(Long id) {
        return CompletedOrderMapper.completedOrderOutputMapping(this.completedOrderDao.getAllData(id));
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
    public CompletedOrderDtoOutput save(ICompletedOrder type) {
        ICompletedOrder completedOrder = this.completedOrderDao.save(type);
        List<IPizza> pizzas = new ArrayList<>();
        for (IPizza pizza : type.getItems()) {
            IPizza output = this.pizzaDao.save(pizza);
            pizzas.add(output);
        }
        return CompletedOrderMapper.completedOrderOutputMapping(new CompletedOrder(completedOrder.getTicket(), pizzas,
                completedOrder.getId(), completedOrder.getTicketId(), completedOrder.getCreationDate(),
                completedOrder.getVersion()));
    }

    @Override
    public List<CompletedOrderDtoOutput> get() {
        List<CompletedOrderDtoOutput> outputs = new ArrayList<>();
        for (ICompletedOrder order : this.completedOrderDao.get()) {
            CompletedOrderDtoOutput output = CompletedOrderMapper.completedOrderOutputMapping(order);
            outputs.add(output);
        }
        return outputs;
    }

    @Override
    public CompletedOrderDtoOutput get(Long id) {
        return CompletedOrderMapper.completedOrderOutputMapping(this.completedOrderDao.get(id));
    }
}
