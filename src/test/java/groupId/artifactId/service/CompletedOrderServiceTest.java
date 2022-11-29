package groupId.artifactId.service;

import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.core.mapper.CompletedOrderMapper;
import groupId.artifactId.dao.CompletedOrderDao;
import groupId.artifactId.dao.PizzaDao;
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
import java.time.Instant;
import java.util.Collections;
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
    private PizzaDao pizzaDao;
    @Mock
    private CompletedOrderMapper completedOrderMapper;

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
        List<ISelectedItem> selectedItems = Collections.singletonList(SelectedItem.builder().id(id).menuItem(menuItem)
                .count(count).createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().id(id).name(name).description(description)
                .size(size).createdAt(creationDate).version(version).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        List<IPizza> pizzas = Collections.singletonList(new Pizza(id, id, name, size, creationDate, version));
        List<PizzaDtoOutput> pizzaDtoOutputs = Collections.singletonList(PizzaDtoOutput.builder().id(id)
                .completedOrderId(id).name(name).size(size).createdAt(creationDate).version(version).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final ICompletedOrder completedOrder = new CompletedOrder(ticket, pizzas, id, id, creationDate, version);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        final CompletedOrderDtoOutput dtoOutput = CompletedOrderDtoOutput.builder().ticket(ticketDtoOutput)
                .items(pizzaDtoOutputs).id(id).ticketId(id).createdAt(creationDate).version(version).build();
        Mockito.when(completedOrderDao.getAllData(id)).thenReturn(completedOrder);
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
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
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
            Assertions.assertEquals(id, output.getCompletedOrderId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void getAllDataRow() {
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
        List<IPizza> pizzas = singletonList(new Pizza(id, id, name, size, creationDate, version));
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final ICompletedOrder completedOrder = new CompletedOrder(ticket, pizzas, id, id, creationDate, version);
        Mockito.when(completedOrderDao.getAllData(id)).thenReturn(completedOrder);

        //test
        ICompletedOrder test = completedOrderService.getAllDataRow(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getTicket());
        Assertions.assertNotNull(test.getItems());
        Assertions.assertNotNull(test.getTicket().getOrder());
        Assertions.assertNotNull(test.getTicket().getOrder().getSelectedItems());
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
        Assertions.assertEquals(id, test.getTicket().getId());
        Assertions.assertEquals(creationDate, test.getTicket().getCreateAt());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        for (ISelectedItem output : test.getTicket().getOrder().getSelectedItems()) {
            Assertions.assertNotNull(output.getMenuItem());
            Assertions.assertNotNull(output.getMenuItem().getPizzaInfo());
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreateAt());
            Assertions.assertEquals(id, output.getMenuItem().getId());
            Assertions.assertEquals(price, output.getMenuItem().getPrice());
            Assertions.assertEquals(creationDate, output.getMenuItem().getCreationDate());
            Assertions.assertEquals(version, output.getMenuItem().getVersion());
            Assertions.assertEquals(id, output.getMenuItem().getPizzaInfo().getId());
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
            Assertions.assertEquals(creationDate, output.getMenuItem().getPizzaInfo().getCreationDate());
            Assertions.assertEquals(version, output.getMenuItem().getPizzaInfo().getVersion());
        }
        for (IPizza output : test.getItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getCompletedOrderId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void isPizzaIdValid() {
        // preconditions
        final long id = 1L;
        Mockito.when(pizzaDao.exist(id)).thenReturn(true);

        //test
        Boolean test = completedOrderService.isPizzaIdValid(id);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(true, test);
    }

    @Test
    void save() {
        // preconditions
        final long id = 1L;
        final String name = "ITALIANO PIZZA";
        final int size = 32;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final ICompletedOrder completedOrder = new CompletedOrder(new Ticket(), Collections.singletonList(new Pizza()), id);
        final Pizza pizza = new Pizza(id, id, name, size);
        final CompletedOrderDtoCrudOutput dtoCrudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
        .createdAt(creationDate).version(version).build();
        Mockito.when(completedOrderDao.save(any(ICompletedOrder.class), any(EntityManager.class))).thenReturn(new CompletedOrder(id, id));
        Mockito.when(pizzaDao.save(any(IPizza.class), any(EntityManager.class))).thenReturn(pizza);
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
        final Instant creationDate = Instant.now();
        List<ICompletedOrder> completedOrders = Collections.singletonList(new CompletedOrder(new Ticket(),
                Collections.singletonList(new Pizza()), id, id, creationDate, version));
        final CompletedOrderDtoCrudOutput crudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).version(version).build();
        Mockito.when(completedOrderDao.get()).thenReturn(completedOrders);
        Mockito.when(completedOrderMapper.outputCrudMapping(any(ICompletedOrder.class))).thenReturn(crudOutput);

        //test
        List<CompletedOrderDtoCrudOutput> test = completedOrderService.get();

        // assert
        Assertions.assertEquals(completedOrders.size(), test.size());
        for (CompletedOrderDtoCrudOutput output : test) {
            Assertions.assertNotNull(output);
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getTicketId());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void testGet() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        ICompletedOrder completedOrders = new CompletedOrder(new Ticket(),
                Collections.singletonList(new Pizza()), id, id, creationDate, version);
        final CompletedOrderDtoCrudOutput crudOutput = CompletedOrderDtoCrudOutput.builder().id(id).ticketId(id)
                .createdAt(creationDate).version(version).build();
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