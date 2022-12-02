package groupId.artifactId.service;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.core.mapper.OrderDataMapper;
import groupId.artifactId.core.mapper.OrderStageMapper;
import groupId.artifactId.dao.OrderDataDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
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
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static groupId.artifactId.core.Constants.ORDER_FINISH_DESCRIPTION;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderDataServiceTest {
    @InjectMocks
    private OrderDataService orderDataService;
    @Mock
    private OrderDataDao orderDataDao;
    @Mock
    private OrderDataMapper orderDataMapper;
    @Mock
    private OrderStageMapper orderStageMapper;
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
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
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
        List<IOrderStage> orderStages = singletonList(OrderStage.builder().id(id).description(stageDescription)
                .creationDate(creationDate).build());
        List<OrderStageDtoOutput> stageDtoOutputs = singletonList(OrderStageDtoOutput.builder().id(id)
                .description(stageDescription).createdAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final IOrderData orderData = OrderData.builder().ticket(ticket).orderHistory(orderStages).id(id).done(done)
                .creationDate(creationDate).build();
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        final OrderDataDtoOutput orderDataDtoOutput = OrderDataDtoOutput.builder().ticket(ticketDtoOutput)
                .orderHistory(stageDtoOutputs).id(id).done(done).createdAt(creationDate).build();
        Mockito.when(orderDataDao.get(id)).thenReturn(orderData);
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
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(id, test.getTicket().getId());
        Assertions.assertEquals(creationDate, test.getTicket().getCreatedAt());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        for (SelectedItemDtoOutput output : test.getTicket().getOrder().getSelectedItems()) {
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
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(stageDescription, output.getDescription());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
        }
    }

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final OrderDataDtoInput orderDataDtoInput = OrderDataDtoInput.builder().ticketId(id).description(ORDER_FINISH_DESCRIPTION).build();
        IOrderStage stage = OrderStage.builder().description(description).id(id).creationDate(creationDate).build();
        List<IOrderStage> stages = new ArrayList<>();
        stages.add(stage);
        final IOrderData orderData = OrderData.builder().id(id).orderHistory(stages).done(done).ticket(ticket)
                .creationDate(creationDate).build();
        final OrderDataDtoCrudOutput dtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(orderStageMapper.inputMapping(any(String.class))).thenReturn(stage);
        Mockito.when(orderDataDao.getOptional(any(Long.class), any(EntityManager.class))).thenReturn(orderData);
        Mockito.when(orderDataDao.update(any(IOrderData.class), any(EntityManager.class))).thenReturn(orderData);
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(dtoOutput);

        //test
        OrderDataDtoCrudOutput test = orderDataService.save(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void create() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final OrderDataDtoInput orderDataDtoInput = OrderDataDtoInput.builder().ticketId(id).ticket(ticket).description(stageDescription).build();
        IOrderStage stages = OrderStage.builder().description(description).id(id).creationDate(creationDate).build();
        final IOrderData orderData = OrderData.builder().id(id).orderHistory(singletonList(stages)).done(done).ticket(ticket)
                .creationDate(creationDate).build();
        Mockito.when(orderStageMapper.inputMapping(any(String.class))).thenReturn(stages);
        Mockito.when(orderDataDao.save(any(IOrderData.class), any(EntityManager.class))).thenReturn(orderData);
        ArgumentCaptor<OrderData> value = ArgumentCaptor.forClass(OrderData.class);
        ArgumentCaptor<EntityManager> value2 = ArgumentCaptor.forClass(EntityManager.class);

        //test
        orderDataService.create(orderDataDtoInput, entityManager);
        Mockito.verify(orderDataDao, times(1)).save(value.capture(), value2.capture());

        // assert
        Assertions.assertEquals(singletonList(stages), value.getValue().getOrderHistory());
        Assertions.assertEquals(ticket, value.getValue().getTicket());
    }

    @Test
    void get() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        List<IOrderStage> orderStages = singletonList(OrderStage.builder().id(id).description(stageDescription)
                .creationDate(creationDate).build());
        IOrderStage stages = OrderStage.builder().description(description).id(id).creationDate(creationDate).build();
        final IOrderData orderData = OrderData.builder().id(id).orderHistory(singletonList(stages)).done(done).ticket(ticket)
                .creationDate(creationDate).orderHistory(orderStages).build();
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).build();
        Mockito.when(orderDataDao.get()).thenReturn(singletonList(orderData));
        Mockito.when(orderDataMapper.outputCrudMapping(any(IOrderData.class))).thenReturn(orderDataDtoOutput);

        //test
        List<OrderDataDtoCrudOutput> test = orderDataService.get();

        // assert
        Assertions.assertEquals(singletonList(orderData).size(), test.size());
        for (OrderDataDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getTicketId());
            Assertions.assertEquals(done, output.getDone());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        List<IOrderStage> orderStages = singletonList(OrderStage.builder().id(id).description(stageDescription)
                .creationDate(creationDate).build());
        IOrderStage stages = OrderStage.builder().description(description).id(id).creationDate(creationDate).build();
        final IOrderData orderData = OrderData.builder().id(id).orderHistory(singletonList(stages)).done(done).ticket(ticket)
                .creationDate(creationDate).orderHistory(orderStages).build();
        final OrderDataDtoCrudOutput orderDataDtoOutput = OrderDataDtoCrudOutput.builder().id(id).ticketId(id).done(done)
                .createdAt(creationDate).build();
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
    }
}