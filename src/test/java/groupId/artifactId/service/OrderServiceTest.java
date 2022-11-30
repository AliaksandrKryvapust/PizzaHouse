package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderMapper;
import groupId.artifactId.core.mapper.SelectedItemMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.TicketDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private TicketDao ticketDao;
    @Mock
    private OrderDataService orderDataService;
    @Mock
    private MenuItemService menuItemService;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private SelectedItemMapper selectedItemMapper;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction transaction;


    @Test
    void getAllData() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        Mockito.when(ticketDao.get(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(ticketDtoOutput);

        //test
        TicketDtoOutput test = orderService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getOrder().getId());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreatedAt());
            Assertions.assertEquals(version, output.getMenuItem().getVersion());
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfo().getId());
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
            Assertions.assertEquals(creationDate, output.getMenuItem().getPizzaInfo().getCreatedAt());
            Assertions.assertEquals(version, output.getMenuItem().getPizzaInfo().getVersion());
        }
    }

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final int count = 5;
        final boolean done = false;
        final String description = "Order accepted";
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String pizzaDescription = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final List<Long> ids = singletonList(id);
        final OrderDtoInput orderDtoInput = OrderDtoInput.builder().selectedItems(singletonList(SelectedItemDtoInput.builder()
                .menuItemId(id).count(count).build())).build();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(pizzaDescription).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).count(count).menuItem(menuItem)
                .createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final TicketDtoCrudOutput crudOutput = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate)
                .build();
        final OrderDataDtoCrudOutput dtoCrudOutput = OrderDataDtoCrudOutput.builder()
                .id(id).ticketId(id).done(done).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(menuItemService.getRow(ids, entityManager)).thenReturn(singletonList(menuItem));
        Mockito.when(selectedItemMapper.inputMapping(any(SelectedItemDtoInput.class), any())).thenReturn(selectedItems.get(0));
        Mockito.when(ticketDao.save(any(ITicket.class), any(EntityManager.class))).thenReturn(ticket);
//        Mockito.when(orderDataService.save(any(OrderDataDtoInput.class))).thenReturn(dtoCrudOutput);
//        ArgumentCaptor<OrderDataDtoInput> value = ArgumentCaptor.forClass(OrderDataDtoInput.class);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(crudOutput);

        //test
        TicketDtoCrudOutput test = orderService.save(orderDtoInput);
//        Mockito.verify(orderDataService, times(1)).save(value.capture());

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
//        Assertions.assertEquals(id, value.getValue().getTicketId());
//        Assertions.assertEquals(done, value.getValue().getDone());
//        Assertions.assertEquals(description, value.getValue().getDescription());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final Instant creationDate = Instant.now();
        List<ITicket> tickets = singletonList(Ticket.builder().id(id).createAt(creationDate).build());
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate).build();
        Mockito.when(ticketDao.get()).thenReturn(tickets);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(output);

        //test
        List<TicketDtoCrudOutput> test = orderService.get();

        // assert
        Assertions.assertEquals(tickets.size(), test.size());
        for (TicketDtoCrudOutput ticket : test) {
            Assertions.assertNotNull(ticket);
            Assertions.assertEquals(id, ticket.getId());
            Assertions.assertEquals(id, ticket.getOrderId());
            Assertions.assertEquals(creationDate, ticket.getCreateAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final Instant creationDate = Instant.now();
        final ITicket ticket = Ticket.builder().id(id).createAt(creationDate).build();
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate).build();
        Mockito.when(ticketDao.get(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(output);

        //test
        TicketDtoCrudOutput test = orderService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
    }
}