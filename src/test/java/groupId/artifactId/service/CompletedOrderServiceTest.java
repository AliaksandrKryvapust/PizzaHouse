package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.CompletedOrderDao;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.ICompletedOrder;
import groupId.artifactId.dao.entity.api.IPizza;
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
class CompletedOrderServiceTest {
    @InjectMocks
    private CompletedOrderService completedOrderService;
    @Mock
    private CompletedOrderDao completedOrderDao;
    @Mock
    private CompletedOrderMapper completedOrderMapper;
    @Mock
    EntityManager entityManager;
    @Mock
    EntityTransaction transaction;

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
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        ICompletedOrder completedOrder = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        List<PizzaDtoOutput> pizzaDtoOutputs = singletonList(PizzaDtoOutput.builder().id(id)
                .name(name).size(size).build());
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        final CompletedOrderDtoOutput dtoOutput = CompletedOrderDtoOutput.builder().ticket(ticketDtoOutput)
                .items(pizzaDtoOutputs).id(id).createdAt(creationDate).build();
        Mockito.when(completedOrderDao.get(id)).thenReturn(completedOrder);
        Mockito.when(completedOrderMapper.outputMapping(any(ICompletedOrder.class))).thenReturn(dtoOutput);

        //test
        CompletedOrderDtoOutput test = completedOrderService.getAllData(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getTicket());
        Assertions.assertNotNull(test.getItems());
        Assertions.assertNotNull(test.getTicket().getOrder());
        Assertions.assertNotNull(test.getTicket().getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
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
        for (PizzaDtoOutput output : test.getItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
        }
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
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        ICompletedOrder completedOrder = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final CompletedOrderDtoCrudOutput dtoCrudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).build();
        Mockito.when(completedOrderDao.save(any(ICompletedOrder.class), any(EntityManager.class))).thenReturn(completedOrder);
        Mockito.when(completedOrderMapper.outputCrudMapping(any(ICompletedOrder.class))).thenReturn(dtoCrudOutput);

        //test
        CompletedOrderDtoCrudOutput test = completedOrderService.create(completedOrder, entityManager);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
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
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        ICompletedOrder completedOrder = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final CompletedOrderDtoCrudOutput dtoCrudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).build();
        Mockito.when(entityManager.getTransaction()).thenReturn(transaction);
        Mockito.when(completedOrderDao.save(any(ICompletedOrder.class), any(EntityManager.class))).thenReturn(completedOrder);
        Mockito.when(completedOrderMapper.outputCrudMapping(any(ICompletedOrder.class))).thenReturn(dtoCrudOutput);

        //test
        CompletedOrderDtoCrudOutput test = completedOrderService.save(completedOrder);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
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
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        ICompletedOrder completedOrders = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final CompletedOrderDtoCrudOutput crudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).build();
        Mockito.when(completedOrderDao.get()).thenReturn(singletonList(completedOrders));
        Mockito.when(completedOrderMapper.outputCrudMapping(any(ICompletedOrder.class))).thenReturn(crudOutput);

        //test
        List<CompletedOrderDtoCrudOutput> test = completedOrderService.get();

        // assert
        Assertions.assertEquals(singletonList(completedOrders).size(), test.size());
        for (CompletedOrderDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getTicketId());
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
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().id(id).name(name).description(description).size(size)
                .creationDate(creationDate).version(version).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        ICompletedOrder completedOrders = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final CompletedOrderDtoCrudOutput crudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).build();
        Mockito.when(completedOrderDao.get(id)).thenReturn(completedOrders);
        Mockito.when(completedOrderMapper.outputCrudMapping(any(ICompletedOrder.class))).thenReturn(crudOutput);

        //test
        CompletedOrderDtoCrudOutput test = completedOrderService.get(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }
}