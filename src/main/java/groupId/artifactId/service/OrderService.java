package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.api.EntityManagerFactoryHibernate;
import groupId.artifactId.dao.api.IOrderDao;
import groupId.artifactId.dao.api.ISelectedItemDao;
import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.OptimisticLockException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderService;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements IOrderService {
    private final ISelectedItemDao selectedItemDao;
    private final IOrderDao orderDao;
    private final ITicketDao ticketDao;
    private final IOrderDataService orderDataService;

    private final TicketMapper ticketMapper;
    private final OrderMapper orderMapper;

    public OrderService(ISelectedItemDao selectedItemDao, IOrderDao orderDao, ITicketDao ticketDao, IOrderDataService orderDataService,
                        TicketMapper ticketMapper, OrderMapper orderMapper) {
        this.selectedItemDao = selectedItemDao;
        this.orderDao = orderDao;
        this.ticketDao = ticketDao;
        this.orderDataService = orderDataService;
        this.ticketMapper = ticketMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public TicketDtoOutput getAllData(Long id) {
        try {
            return ticketMapper.outputMapping(this.ticketDao.getAllData(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Ticket at Service by id" + id, e);
        }
    }

    @Override
    public Boolean isItemIdValid(Long id) {
        try {
            return this.selectedItemDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Selected Item at Service by id " + id, e);
        }
    }

    @Override
    public Boolean isOrderIdValid(Long id) {
        try {
            return this.orderDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Order at Service by id " + id, e);
        }
    }

    @Override
    public Boolean isTicketIdValid(Long id) {
        try {
            return this.ticketDao.exist(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to check Ticket at Service by id " + id, e);
        }
    }

    @Override
    public TicketDtoCrudOutput save(OrderDtoInput orderDtoInput) {
        try {
            IOrder orderId = this.orderDao.save(new Order(), EntityManagerFactoryHibernate.getEntityManager());
            IOrder input = orderMapper.inputMapping(orderDtoInput, orderId.getId());
            List<ISelectedItem> items = new ArrayList<>();
            for (ISelectedItem selectedItem : input.getSelectedItems()) {
                ISelectedItem output = this.selectedItemDao.save(selectedItem, EntityManagerFactoryHibernate.getEntityManager());
                items.add(output);
            }
            ITicket ticket = this.ticketDao.save(new Ticket(orderId.getId()),EntityManagerFactoryHibernate.getEntityManager());
            orderDataService.save(OrderDataDtoInput.builder().ticketId(ticket.getId()).done(false)
                    .description("Order accepted").build());
            return ticketMapper.outputCrudMapping(new Ticket(new Order(items, orderId.getId()), ticket.getId(), ticket.getOrderId()));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to save Order" + orderDtoInput, e);
        }
    }

    @Override
    public List<TicketDtoCrudOutput> get() {
        try {
            List<TicketDtoCrudOutput> temp = new ArrayList<>();
            for (ITicket ticket : this.ticketDao.get()) {
                TicketDtoCrudOutput outPut = ticketMapper.outputCrudMapping(ticket);
                temp.add(outPut);
            }
            return temp;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("Failed to get List of Ticket`s at Service", e);
        }
    }

    @Override
    public TicketDtoCrudOutput get(Long id) {
        try {
            return ticketMapper.outputCrudMapping(this.ticketDao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to get Ticket at Service by id" + id, e);
        }
    }

    @Override
    public void delete(String id, String delete) {
        try {
            this.selectedItemDao.delete(Long.valueOf(id), Boolean.valueOf(delete));
            this.ticketDao.delete(Long.valueOf(id), Boolean.valueOf(delete));
            this.orderDao.delete(Long.valueOf(id), Boolean.valueOf(delete));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete Order with id:" + id, e);
        }
    }
}
