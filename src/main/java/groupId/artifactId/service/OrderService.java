package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
import groupId.artifactId.core.mapper.OrderMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.api.IOrderDao;
import groupId.artifactId.dao.api.ISelectedItemDao;
import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.service.api.IOrderService;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements IOrderService {
    private final ISelectedItemDao selectedItemDao;
    private final IOrderDao orderDao;
    private final ITicketDao ticketDao;

    public OrderService(ISelectedItemDao selectedItemDao, IOrderDao orderDao, ITicketDao ticketDao) {
        this.selectedItemDao = selectedItemDao;
        this.orderDao = orderDao;
        this.ticketDao = ticketDao;
    }

    @Override
    public TicketDtoOutPut getAllData(Long id) {
        return TicketMapper.ticketOutputMapping(this.ticketDao.getAllData(id));
    }

    @Override
    public Boolean isItemIdValid(Long id) {
        return this.selectedItemDao.exist(id);
    }

    @Override
    public Boolean isOrderIdValid(Long id) {
        return this.orderDao.exist(id);
    }

    @Override
    public Boolean isTicketIdValid(Long id) {
        return this.ticketDao.exist(id);
    }

    @Override
    public TicketDtoOutPut save(OrderDtoInput orderDtoInput) {
        IOrder orderId = this.orderDao.save(new Order());
        IOrder input = OrderMapper.orderInputMapping(orderDtoInput, orderId.getId());
        List<ISelectedItem> items = new ArrayList<>();
        for (ISelectedItem selectedItem : input.getSelectedItems()) {
            ISelectedItem output = this.selectedItemDao.save(selectedItem);
            items.add(output);
        }
        ITicket ticket = this.ticketDao.save(new Ticket(orderId.getId()));
        return TicketMapper.ticketOutputMapping(new Ticket(new Order(items, orderId.getId()), ticket.getId(), ticket.getOrderId()));
    }

    @Override
    public List<TicketDtoOutPut> get() {
        List<TicketDtoOutPut> temp = new ArrayList<>();
        for (ITicket ticket : this.ticketDao.get()) {
            TicketDtoOutPut outPut = TicketMapper.ticketOutputMapping(ticket);
            temp.add(outPut);
        }
        return temp;
    }

    @Override
    public TicketDtoOutPut get(Long id) {
        return TicketMapper.ticketOutputMapping(this.ticketDao.get(id));
    }

    @Override
    public void delete(String id, String version, String delete) {
        this.selectedItemDao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
        this.ticketDao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
        this.orderDao.delete(Long.valueOf(id), Integer.valueOf(version), Boolean.valueOf(delete));
    }
}
