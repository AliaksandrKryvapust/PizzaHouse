package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.CompletedOrderDtoCrudOutput;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CompletedOrderMapperTest {
    @InjectMocks
    private CompletedOrderMapper completedOrderMapper;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private PizzaMapper pizzaMapper;

    @Test
    void inputMapping() {
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
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        final List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem)
                .count(count).createAt(creationDate).build());
        List<IOrderStage> orderStages = singletonList(OrderStage.builder().id(id).description(stageDescription)
                .creationDate(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final IOrderData orderData = OrderData.builder().ticket(ticket).orderHistory(orderStages).id(id).done(done)
                .creationDate(creationDate).build();

        //test
        ICompletedOrder test = completedOrderMapper.inputMapping(orderData);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getTicket());
        Assertions.assertNotNull(test.getItems());
        Assertions.assertEquals(creationDate, test.getTicket().getCreateAt());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        for (ISelectedItem output : test.getTicket().getOrder().getSelectedItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreateAt());
        }
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        final List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem)
                .count(count).createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        ICompletedOrder completedOrders = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();

        //test
        CompletedOrderDtoCrudOutput test = completedOrderMapper.outputCrudMapping(completedOrders);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
    }

    @Test
    void outputMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final int count = 10;
        final double price = 18.0;
        final String name = "ITALIANO PIZZA";
        final String description = "Mozzarella cheese, basilica, ham";
        final int size = 32;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = Collections.singletonList(SelectedItem.builder().id(id).menuItem(menuItem)
                .count(count).createAt(creationDate).build());
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        List<IPizza> pizzas = singletonList(Pizza.builder().id(id).name(name).size(size).build());
        ICompletedOrder completedOrders = CompletedOrder.builder().items(pizzas).ticket(ticket).id(id)
                .creationDate(creationDate).build();
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
        final PizzaDtoOutput pizzaDtoOutputs = PizzaDtoOutput.builder().id(id)
                .name(name).size(size).build();
        Mockito.when(pizzaMapper.outputMapping(any(IPizza.class))).thenReturn(pizzaDtoOutputs);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(ticketDtoOutput);

        //test
        CompletedOrderDtoOutput test = completedOrderMapper.outputMapping(completedOrders);

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
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
        }
        for (PizzaDtoOutput output : test.getItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
        }
    }
}