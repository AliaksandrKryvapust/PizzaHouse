package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.dao.OrderDataDao;
import groupId.artifactId.dao.OrderStageDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
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
    @Mock
    private CompletedOrderMapper completedOrderMapper;
    @Mock
    private OrderDataMapper orderDataMapper;

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
        List<ISelectedItem> selectedItems = singletonList(new SelectedItem(new MenuItem(id,
                new PizzaInfo(id, name, description, size, creationDate, version), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version));
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price).pizzaInfoId(id)
                .createdAt(creationDate).version(version).menuId(id).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).menuItemId(id).orderId(id).count(count).createdAt(creationDate).version(version).build());
        List<IOrderStage> orderStages = singletonList(new OrderStage(id, id, stageDescription, creationDate, version));
        List<OrderStageDtoOutput> stageDtoOutputs = singletonList(new OrderStageDtoOutput(id, id, stageDescription, creationDate, version));
        final IOrderData orderData = new OrderData(new Ticket(new Order(selectedItems, id, creationDate, version), id,
                orderId, creationDate, version), orderStages, id, id, done, creationDate, version);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id, creationDate, version);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id).orderId(orderId)
                .createdAt(creationDate).version(version).build();
        final OrderDataDtoOutput orderDataDtoOutput = new OrderDataDtoOutput(ticketDtoOutput, stageDtoOutputs, id, id,
                done, creationDate, version);
        Mockito.when(orderDataDao.getAllData(id)).thenReturn(orderData);
        Mockito.when(orderDataMapper.outputMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

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
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getTicket().getId());
        Assertions.assertEquals(orderId, test.getTicket().getOrderId());
        Assertions.assertEquals(creationDate, test.getTicket().getCreatedAt());
        Assertions.assertEquals(version, test.getTicket().getVersion());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        Assertions.assertEquals(creationDate, test.getTicket().getOrder().getCreatedAt());
        Assertions.assertEquals(version, test.getTicket().getOrder().getVersion());
        for (SelectedItemDtoOutput output : test.getTicket().getOrder().getSelectedItems()) {
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
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfoId());
            Assertions.assertEquals(id, output.getMenuItem().getMenuId());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreatedAt());
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
            Assertions.assertEquals(creationDate, output.getCreatedAt());
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
        final Instant creationDate = Instant.now();
        final int version = 3;
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        List<IOrderStage> stages = singletonList(new OrderStage(description));
        final IOrderData orderData = new OrderData(stages, id, done);
        final OrderDataDtoCrudOutput dtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        final OrderData orderDataOutput = new OrderData(id, id, done);
        final OrderStage orderStage = new OrderStage(id, id, description);
        Mockito.when(orderDataMapper.inputMapping(any(OrderDataDtoInput.class))).thenReturn(orderData);
        Mockito.when(orderDataDao.doesTicketExist(any(Long.class))).thenReturn(false);
        Mockito.when(orderDataDao.save(any(IOrderData.class))).thenReturn(orderDataOutput);
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(orderStage);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(dtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.save(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
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
        List<IOrderStage> stages = singletonList(new OrderStage(description));
        final IOrderData orderData = new OrderData(stages, id, done);
        final OrderDataDtoCrudOutput dtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        final OrderData orderDataOutput = new OrderData(id, id, done, creationDate, version);
        final OrderStage orderStage = new OrderStage(id, id, description);
        Mockito.when(orderDataMapper.inputMapping(any(OrderDataDtoInput.class))).thenReturn(orderData);
        Mockito.when(orderDataDao.doesTicketExist(any(Long.class))).thenReturn(true);
        Mockito.when(orderDataDao.getDataByTicket(any(Long.class))).thenReturn(orderDataOutput);
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(orderStage);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(dtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.save(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        List<IOrderData> orderData = singletonList(new OrderData(new Ticket(), singletonList(new OrderStage()),
                id, id, done, creationDate, version));
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        Mockito.when(orderDataDao.get()).thenReturn(orderData);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

        //test
        List<OrderDataDtoCrudOutput> test = orderDataService.get();

        // assert
        Assertions.assertEquals(orderData.size(), test.size());
        for (OrderDataDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getTicketId());
            Assertions.assertEquals(done, output.getDone());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
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
        final IOrderData orderData = new OrderData(new Ticket(), singletonList(new OrderStage()),
                id, id, done, creationDate, version);
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        Mockito.when(orderDataDao.get(id)).thenReturn(orderData);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
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
        List<IOrderStage> stages = singletonList(new OrderStage(description));
        final IOrderData orderData = new OrderData(stages, id, done);
        final IOrderData orderDataUpdate = new OrderData(id, id, done);
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        final CompletedOrder completedOrder = new CompletedOrder(new Ticket(new Order(singletonList(new SelectedItem(
                new MenuItem(id, new PizzaInfo(name, pizzaDescription, size), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version)), id, creationDate, version),
                id, id, creationDate, version), singletonList(new Pizza(
                id, id, name, size, creationDate, version)), id, id, creationDate, version);
        final OrderData orderDataOutput = new OrderData(new Ticket(new Order(singletonList(new SelectedItem(
                new MenuItem(id, new PizzaInfo(name, pizzaDescription, size), price, id, creationDate, version, id),
                id, id, id, count, creationDate, version)), id, creationDate, version),
                id, id, creationDate, version), singletonList(new OrderStage()),
                id, id, done, creationDate, version);
        final OrderStage orderStage = new OrderStage(id, id, description);
        Mockito.when(orderDataMapper.inputMapping(any(OrderDataDtoInput.class))).thenReturn(orderData);
        Mockito.when(orderDataDao.update(any(IOrderData.class), any(Long.class), any(Integer.class))).thenReturn(orderDataUpdate);
        Mockito.when(completedOrderMapper.inputMapping(any(IOrderData.class))).thenReturn(completedOrder);
        Mockito.when(orderDataDao.getAllData(any(Long.class))).thenReturn(orderDataOutput);
        ArgumentCaptor<ICompletedOrder> value = ArgumentCaptor.forClass(ICompletedOrder.class);
        Mockito.when(orderStageDao.doesStageExist(eq(Long.valueOf(inputId)), eq(description))).thenReturn(false);
        Mockito.when(orderStageDao.save(any(IOrderStage.class))).thenReturn(orderStage);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.update(orderDataDtoInput, inputId, inputVersion);
        Mockito.verify(completedOrderService, times(1)).save(value.capture());

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
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
        final int version = 1;
        final Instant creationDate = Instant.now();
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        List<IOrderStage> stages = singletonList(new OrderStage(description));
        final IOrderData orderData = new OrderData(stages, id, done);
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).version(version).build();
        Mockito.when(orderDataMapper.inputMapping(any(OrderDataDtoInput.class))).thenReturn(orderData);
        Mockito.when(orderStageDao.doesStageExist(any(Long.class), any(String.class))).thenReturn(true);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.update(orderDataDtoInput, inputId, inputVersion);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
    }
}