package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
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

import static groupId.artifactId.core.Constants.ORDER_START_DESCRIPTION;

public class OrderService implements IOrderService {
    private final ITicketDao ticketDao;
    private final IOrderDataService orderDataService;
    private final IMenuItemService menuItemService;
    private final EntityManager entityManager;
    private final TicketMapper ticketMapper;
    private final SelectedItemMapper selectedItemMapper;

    public OrderService(ITicketDao ticketDao, IOrderDataService orderDataService, IMenuItemService menuItemService,
                        EntityManager entityManager, TicketMapper ticketMapper, SelectedItemMapper selectedItemMapper) {
        this.ticketDao = ticketDao;
        this.orderDataService = orderDataService;
        this.menuItemService = menuItemService;
        this.entityManager = entityManager;
        this.ticketMapper = ticketMapper;
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
    public ITicket getRow(Long id) {
        try {
            return this.ticketDao.get(id);
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
            List<ISelectedItem> inputSelectedItems = orderDtoInput.getSelectedItems().stream()
                    .map((i) -> selectedItemMapper.inputMapping(i, menuItems)).collect(Collectors.toList());
            entityManager.getTransaction().begin();
            IOrder newOrder = Order.builder().selectedItems(inputSelectedItems).build();
            ITicket ticket = this.ticketDao.save(Ticket.builder().order(newOrder).build(), this.entityManager);
            OrderDataDtoInput dtoInput = OrderDataDtoInput.builder().ticketId(ticket.getId())
                    .description(ORDER_START_DESCRIPTION).ticket(ticket).build();
            orderDataService.create(dtoInput, this.entityManager);
            entityManager.getTransaction().commit();
            return ticketMapper.outputCrudMapping(ticket);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (NoContentException e) {
            throw new NoContentException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
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
}
