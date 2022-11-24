package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderMapper;
import groupId.artifactId.core.mapper.TicketMapper;
import groupId.artifactId.dao.OrderDao;
import groupId.artifactId.dao.SelectedItemDao;
import groupId.artifactId.dao.TicketDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IOrder;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private SelectedItemDao selectedItemDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private TicketDao ticketDao;
    @Mock
    private OrderDataService orderDataService;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private OrderMapper orderMapper;


    @Test
    void getAllData() {
        // preconditions
        final long id = 1L;
        final long orderId = 1L;
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
        List<ISelectedItem> selectedItems = singletonList(new SelectedItem(menuItem, id, id, id, count, creationDate, version));
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).menuItemId(id).orderId(id).count(count).createdAt(creationDate).version(version).build());
        final ITicket ticket = new Ticket(new Order(selectedItems, id, creationDate, version), id, orderId, creationDate, version);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id, creationDate, version);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id).orderId(orderId)
                .createdAt(creationDate).version(version).build();
        Mockito.when(ticketDao.getAllData(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(ticketDtoOutput);

        //test
        TicketDtoOutput test = orderService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(orderId, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getOrder().getId());
        Assertions.assertEquals(creationDate, test.getOrder().getCreatedAt());
        Assertions.assertEquals(version, test.getOrder().getVersion());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderId());
            Assertions.assertEquals(id, output.getMenuItemId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(version, output.getVersion());
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
    void isItemIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(selectedItemDao.exist(id)).thenReturn(true);

        //test
        Boolean test = orderService.isItemIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void isOrderIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(orderDao.exist(id)).thenReturn(true);

        //test
        Boolean test = orderService.isOrderIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void isTicketIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(ticketDao.exist(id)).thenReturn(true);

        //test
        Boolean test = orderService.isTicketIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final int count = 5;
        final boolean done = false;
        final String description = "Order accepted";
        final int version = 1;
        final Instant creationDate = Instant.now();
        final OrderDtoInput orderDtoInput = OrderDtoInput.builder().selectedItems(singletonList(SelectedItemDtoInput.builder()
                .menuItemId(id).count(count).build())).build();
        final Order order = new Order(id);
        final Order orderOutput = new Order(singletonList(new SelectedItem(id, id, count)), id);
        final SelectedItem selectedItem = new SelectedItem(id, id, id, count);
        final Ticket ticket = new Ticket(id, id);
        final TicketDtoCrudOutput crudOutput = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate)
                .version(version).build();
        final OrderDataDtoCrudOutput dtoCrudOutput = OrderDataDtoCrudOutput.builder()
                .id(id).ticketId(id).done(done).build();
        Mockito.when(orderDao.save(any(IOrder.class), any(EntityManager.class))).thenReturn(order);
        Mockito.when(orderMapper.inputMapping(any(OrderDtoInput.class), any(Long.class))).thenReturn(orderOutput);
        Mockito.when(selectedItemDao.save(any(ISelectedItem.class), any(EntityManager.class))).thenReturn(selectedItem);
        Mockito.when(ticketDao.save(any(ITicket.class), any(EntityManager.class))).thenReturn(ticket);
        Mockito.when(orderDataService.save(any(OrderDataDtoInput.class))).thenReturn(dtoCrudOutput);
        ArgumentCaptor<OrderDataDtoInput> value = ArgumentCaptor.forClass(OrderDataDtoInput.class);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(crudOutput);

        //test
        TicketDtoCrudOutput test = orderService.save(orderDtoInput);
        Mockito.verify(orderDataService, times(1)).save(value.capture());

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(id, value.getValue().getTicketId());
        Assertions.assertEquals(done, value.getValue().getDone());
        Assertions.assertEquals(description, value.getValue().getDescription());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        List<ITicket> tickets = singletonList(new Ticket(id, id, creationDate, version));
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate)
                .version(version).build();
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
            Assertions.assertEquals(version, ticket.getVersion());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final ITicket ticket = new Ticket(id, id, creationDate, version);
        final TicketDtoCrudOutput output = TicketDtoCrudOutput.builder().id(id).orderId(id).createAt(creationDate)
                .version(version).build();
        Mockito.when(ticketDao.get(id)).thenReturn(ticket);
        Mockito.when(ticketMapper.outputCrudMapping(any(ITicket.class))).thenReturn(output);

        //test
        TicketDtoCrudOutput test = orderService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
        Assertions.assertEquals(version, test.getVersion());
    }

    @Test
    void delete() {
        final String inputId = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Long> valueIdT = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDeleteT = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Long> valueIdO = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> valueDeleteO = ArgumentCaptor.forClass(Boolean.class);

        //test
        orderService.delete(inputId, delete);
        Mockito.verify(selectedItemDao, times(1)).delete(valueId.capture(), valueDelete.capture(), any(EntityManager.class));
        Mockito.verify(ticketDao, times(1)).delete(valueIdT.capture(), valueDeleteT.capture(), any(EntityManager.class));
        Mockito.verify(orderDao, times(1)).delete(valueIdO.capture(), valueDeleteO.capture(), any(EntityManager.class));

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
        Assertions.assertEquals(Long.valueOf(inputId), valueIdT.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDeleteT.getValue());
        Assertions.assertEquals(Long.valueOf(inputId), valueIdO.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDeleteO.getValue());
    }
}