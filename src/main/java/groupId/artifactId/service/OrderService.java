package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderMapper;
import groupId.artifactId.core.mapper.SelectedItemMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.api.ITicketDao;
import groupId.artifactId.dao.entity.Order;
import groupId.artifactId.dao.entity.Ticket;
import groupId.artifactId.dao.entity.api.IMenuItem;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import groupId.artifactId.exceptions.DaoException;
import groupId.artifactId.exceptions.NoContentException;
import groupId.artifactId.exceptions.ServiceException;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IOrderDataService;
import groupId.artifactId.service.api.IOrderService;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {
    private final ITicketDao ticketDao;
    private final IOrderDataService orderDataService;

    private final IMenuItemService menuItemService;
    private final EntityManager entityManager;
    private final TicketMapper ticketMapper;
    private final OrderMapper orderMapper;
    private final SelectedItemMapper selectedItemMapper;
    private Long idToDelete;

    public OrderService(ITicketDao ticketDao, IOrderDataService orderDataService, IMenuItemService menuItemService,
                        EntityManager entityManager, TicketMapper ticketMapper, OrderMapper orderMapper,
                        SelectedItemMapper selectedItemMapper) {
        this.ticketDao = ticketDao;
        this.orderDataService = orderDataService;
        this.menuItemService = menuItemService;
        this.entityManager = entityManager;
        this.ticketMapper = ticketMapper;
        this.orderMapper = orderMapper;
        this.selectedItemMapper = selectedItemMapper;
    }

    @Override
    public TicketDtoOutput getAllData(Long id) {
        try {
            return ticketMapper.outputMapping(this.ticketDao.get(id));
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (Exception e) {
            throw new ServiceException("Failed to getAll data from Ticket at Service by id" + id, e);
        }
    }

    @Override
    public TicketDtoCrudOutput save(OrderDtoInput orderDtoInput) {
        try {
            List<Long> menuItemsId = orderDtoInput.getSelectedItems().stream().map(SelectedItemDtoInput::getMenuItemId)
                    .collect(Collectors.toList());
            List<IMenuItem> menuItems = this.menuItemService.getRow(menuItemsId, this.entityManager);
            entityManager.getTransaction().begin();
            List<ISelectedItem> input = orderDtoInput.getSelectedItems().stream()
                    .map((i) -> selectedItemMapper.inputMapping(i, menuItems)).collect(Collectors.toList());
            List<ISelectedItem> savedItems = this.ticketDao.saveItems(input, this.entityManager);
            Ticket ticket = (Ticket) this.ticketDao.save(Ticket.builder().order(new Order()).build(), this.entityManager);
            entityManager.getTransaction().commit();
            idToDelete = ticket.getId();
            entityManager.getTransaction().begin();
            IOrder updatedOrder = this.ticketDao.update(ticket.getOrder(), savedItems, this.entityManager);
            ticket.setOrder(updatedOrder);
//            IOrder orderInput = orderMapper.inputMapping(orderDtoInput, menuItems);
//            Ticket ticket = (Ticket) this.ticketDao.save(Ticket.builder().order(new Order()).build(), this.entityManager);
//            entityManager.getTransaction().commit();
//            idToDelete=ticket.getId();
//            entityManager.getTransaction().begin();
//            Order savedOrder = (Order) ticket.getOrder();
//            savedOrder.setSelectedItems(orderInput.getSelectedItems());
//            IOrder orderOutput = this.ticketDao.saveItems(savedOrder, this.entityManager);
//            ticket.setOrder(orderOutput);


//            orderDataService.save(OrderDataDtoInput.builder().ticketId(ticket.getId()).done(false)
//                    .description("Order accepted").build());


            entityManager.getTransaction().commit();
            return ticketMapper.outputCrudMapping(ticket);
        } catch (DaoException e) {
            this.delete();
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            this.delete();
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            this.delete();
            throw new IllegalStateException(e);
        } catch (Exception e) {
            this.delete();
            throw new ServiceException("Failed to save Order" + orderDtoInput, e);
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<TicketDtoCrudOutput> get() {
        try {
            List<TicketDtoCrudOutput> temp = new ArrayList<>();
            for (ITicket ticket : this.ticketDao.get()) {
                TicketDtoCrudOutput output = ticketMapper.outputCrudMapping(ticket);
                temp.add(output);
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

    private void delete() {
        this.ticketDao.delete(idToDelete, false, this.entityManager);
        this.entityManager.getTransaction().commit();
    }
}
