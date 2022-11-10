package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.input.SelectedItemDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.core.dto.output.TicketDtoOutPut;
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

import java.time.Instant;
import java.util.Collections;
import java.util.List;

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
        List<ISelectedItem> selectedItems = Collections.singletonList(new SelectedItem(new MenuItem(id,
                new PizzaInfo(id, name, description, size, creationDate, version), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version));
        final ITicket ticket = new Ticket(new Order(selectedItems, id, creationDate, version), id, orderId, creationDate, version);
        Mockito.when(ticketDao.getAllData(id)).thenReturn(ticket);

        //test
        TicketDtoOutPut test = orderService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(orderId, test.getOrderId());
        Assertions.assertEquals(creationDate, test.getCreateAt());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getOrder().getId());
        Assertions.assertEquals(creationDate, test.getOrder().getCreationDate());
        Assertions.assertEquals(version, test.getOrder().getVersion());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderId());
            Assertions.assertEquals(id, output.getMenuItemId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreateAt());
            Assertions.assertEquals(version, output.getVersion());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfoId());
            Assertions.assertEquals(id, output.getMenuItem().getMenuId());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreation_date());
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
        final OrderDtoInput orderDtoInput = new OrderDtoInput(Collections.singletonList(new SelectedItemDtoInput(id, count)));
        Mockito.when(orderDao.save(any(IOrder.class))).thenReturn(new Order(id));
        Mockito.when(selectedItemDao.save(any(ISelectedItem.class))).thenReturn(new SelectedItem(id, id, id, count));
        Mockito.when(ticketDao.save(any(ITicket.class))).thenReturn(new Ticket(id, id));
        Mockito.when(orderDataService.save(any(OrderDataDtoInput.class))).thenReturn(new OrderDataDtoOutput());
        ArgumentCaptor<OrderDataDtoInput> value = ArgumentCaptor.forClass(OrderDataDtoInput.class);

        //test
        TicketDtoOutPut test = orderService.save(orderDtoInput);
        Mockito.verify(orderDataService, times(1)).save(value.capture());

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrder());
        Assertions.assertNotNull(test.getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getOrderId());
        Assertions.assertEquals(id, test.getOrder().getId());
        for (SelectedItemDtoOutput output : test.getOrder().getSelectedItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderId());
            Assertions.assertEquals(id, output.getMenuItemId());
            Assertions.assertEquals(count, output.getCount());
        }
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
        List<ITicket> tickets = Collections.singletonList(new Ticket(id, id, creationDate, version));
        Mockito.when(ticketDao.get()).thenReturn(tickets);

        //test
        List<TicketDtoOutPut> test = orderService.get();

        // assert
        Assertions.assertEquals(tickets.size(), test.size());
        for (TicketDtoOutPut ticket: test) {
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
        Mockito.when(ticketDao.get(id)).thenReturn(ticket);

        //test
        TicketDtoOutPut test = orderService.get(id);

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
        final String version = "1";
        final String delete = "false";
        ArgumentCaptor<Long> valueId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> valueVersion = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> valueDelete = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Long> valueIdT = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> valueVersionT = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> valueDeleteT = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Long> valueIdO = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> valueVersionO = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Boolean> valueDeleteO = ArgumentCaptor.forClass(Boolean.class);

        //test
        orderService.delete(inputId, version, delete);
        Mockito.verify(selectedItemDao, times(1)).delete(valueId.capture(), valueVersion.capture(), valueDelete.capture());
        Mockito.verify(ticketDao, times(1)).delete(valueIdT.capture(), valueVersionT.capture(), valueDeleteT.capture());
        Mockito.verify(orderDao, times(1)).delete(valueIdO.capture(), valueVersionO.capture(), valueDeleteO.capture());

        // assert
        Assertions.assertEquals(Long.valueOf(inputId), valueId.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersion.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDelete.getValue());
        Assertions.assertEquals(Long.valueOf(inputId), valueIdT.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersionT.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDeleteT.getValue());
        Assertions.assertEquals(Long.valueOf(inputId), valueIdO.getValue());
        Assertions.assertEquals(Integer.valueOf(version), valueVersionO.getValue());
        Assertions.assertEquals(Boolean.valueOf(delete), valueDeleteO.getValue());
    }
}