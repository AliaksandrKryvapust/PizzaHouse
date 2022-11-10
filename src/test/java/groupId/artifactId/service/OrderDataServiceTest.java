package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.OrderDataDtoOutput;
import groupId.artifactId.core.dto.output.OrderStageDtoOutput;
import groupId.artifactId.core.dto.output.SelectedItemDtoOutput;
import groupId.artifactId.dao.OrderDataDao;
import groupId.artifactId.dao.OrderStageDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderDataServiceTest {
    @InjectMocks
    private OrderDataService orderDataService;
    @Mock
    private OrderDataDao orderDataDao;
    @Mock
    private OrderStageDao orderStageDao;
    @Mock
    private CompletedOrderService completedOrderService;

    @BeforeEach
    public void init() {
        orderDataService = new OrderDataService(orderDataDao, orderStageDao, completedOrderService);
    }

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
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        List<ISelectedItem> selectedItems = Collections.singletonList(new SelectedItem(new MenuItem(id,
                new PizzaInfo(id, name, description, size, creationDate, version), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version));
        List<IOrderStage> orderStages = Collections.singletonList(new OrderStage(id, id, stageDescription, creationDate, version));
        final IOrderData orderData = new OrderData(new Ticket(new Order(selectedItems, id, creationDate, version), id,
                orderId, creationDate, version), orderStages, id, id, done, creationDate, version);
        Mockito.when(orderDataDao.getAllData(id)).thenReturn(orderData);

        //test
        OrderDataDtoOutput test = orderDataService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getTicket());
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertNotNull(test.getTicket().getOrder());
        Assertions.assertNotNull(test.getTicket().getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getTicket().getId());
        Assertions.assertEquals(orderId, test.getTicket().getOrderId());
        Assertions.assertEquals(creationDate, test.getTicket().getCreateAt());
        Assertions.assertEquals(version, test.getTicket().getVersion());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        Assertions.assertEquals(creationDate, test.getTicket().getOrder().getCreationDate());
        Assertions.assertEquals(version, test.getTicket().getOrder().getVersion());
        for (SelectedItemDtoOutput output : test.getTicket().getOrder().getSelectedItems()) {
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
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderDataId());
            Assertions.assertEquals(stageDescription, output.getDescription());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void isIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(orderDataDao.exist(id)).thenReturn(true);

        //test
        Boolean test = orderDataService.isIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void isOrderStageIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(orderStageDao.exist(id)).thenReturn(true);

        //test
        Boolean test = orderDataService.isOrderStageIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void isTicketIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(orderDataDao.doesTicketExist(id)).thenReturn(true);

        //test
        Boolean test = orderDataService.isTicketIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void exist() {
        // preconditions
        final long id = 1L;
        final String stage = "stage #";
        Mockito.when(orderStageDao.doesStageExist(id, stage)).thenReturn(true);

        //test
        Boolean test = orderDataService.exist(id, stage);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void saveTrueCondition() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final String description = "Order accepted";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        Mockito.when(orderDataDao.doesTicketExist(any(Long.class))).thenReturn(false);
        Mockito.when(orderDataDao.save(any(IOrderData.class))).thenReturn(new OrderData(id, id, done));
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(new OrderStage(id, id, description));

        //test
        OrderDataDtoOutput test = orderDataService.save(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(description, output.getDescription());
        }
    }

    @Test
    void saveFalseCondition() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final String description = "Order accepted";
        final Instant creationDate = Instant.now();
        final int version = 3;
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        Mockito.when(orderDataDao.doesTicketExist(any(Long.class))).thenReturn(true);
        Mockito.when(orderDataDao.getDataByTicket(any(Long.class))).thenReturn(new OrderData(id, id, done, creationDate, version));
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(new OrderStage(id, id, description));

        //test
        OrderDataDtoOutput test = orderDataService.save(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(description, output.getDescription());
        }
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        List<IOrderData> orderData = Collections.singletonList(new OrderData(new Ticket(), Collections.singletonList(new OrderStage()),
                id, id, done, creationDate, version));
        Mockito.when(orderDataDao.get()).thenReturn(orderData);

        //test
        List<OrderDataDtoOutput> test = orderDataService.get();

        // assert
        Assertions.assertEquals(orderData.size(), test.size());
        for (OrderDataDtoOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getTicketId());
            Assertions.assertEquals(done, output.getDone());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final IOrderData orderData = new OrderData(new Ticket(), Collections.singletonList(new OrderStage()),
                id, id, done, creationDate, version);
        Mockito.when(orderDataDao.get(id)).thenReturn(orderData);

        //test
        OrderDataDtoOutput test = orderDataService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());

    }

    @Test
    void updateTrueCondition() {
        // preconditions
        final long id = 1L;
        final boolean done = true;
        final String inputId = "1";
        final String inputVersion = "1";
        final String description = "Order accepted";
        final String name = "ITALIANO PIZZA";
        final String pizzaDescription = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final int count = 3;
        final double price = 20.0;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        Mockito.when(orderDataDao.update(any(IOrderData.class), eq(Long.valueOf(inputId)), eq(Integer.valueOf(inputVersion)))).
                thenReturn(new OrderData(id, id, done));
        Mockito.when(orderDataDao.getAllData(any(Long.class))).
                thenReturn(new OrderData(new Ticket(new Order(Collections.singletonList(new SelectedItem(
                        new MenuItem(id, new PizzaInfo(name, pizzaDescription, size), price, id, creationDate, version, id),
                        id, id, id, count, creationDate, version)), id, creationDate, version),
                        id, id, creationDate, version), Collections.singletonList(new OrderStage()),
                        id, id, done, creationDate, version));
        ArgumentCaptor<ICompletedOrder> value = ArgumentCaptor.forClass(ICompletedOrder.class);
        Mockito.when(orderStageDao.doesStageExist(eq(Long.valueOf(inputId)), eq(description))).thenReturn(false);
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(new OrderStage(id, id, description));

        //test
        OrderDataDtoOutput test = orderDataService.update(orderDataDtoInput, inputId, inputVersion);
        Mockito.verify(completedOrderService, times(1)).save(value.capture());

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(description, output.getDescription());
        }
        Assertions.assertNotNull(value.getValue().getTicket());
        Assertions.assertNotNull(value.getValue().getItems());
        Assertions.assertEquals(id, value.getValue().getTicketId());
        for (IPizza output : value.getValue().getItems()) {
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
        }
    }

    @Test
    void updateFalseCondition() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final String inputId = "1";
        final String inputVersion = "1";
        final String description = "Order accepted";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        Mockito.when(orderStageDao.doesStageExist(any(Long.class), any(String.class))).thenReturn(true);

        //test
        OrderDataDtoOutput test = orderDataService.update(orderDataDtoInput, inputId, inputVersion);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(description, output.getDescription());
        }
    }
}