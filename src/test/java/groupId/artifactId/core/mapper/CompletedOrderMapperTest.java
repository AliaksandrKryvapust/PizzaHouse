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
        List<IOrderStage> orderStages = singletonList(new OrderStage(id, id, stageDescription, creationDate, version));
        final IOrderData orderData = new OrderData(new Ticket(new Order(selectedItems, id, creationDate, version), id,
                orderId, creationDate, version), orderStages, id, id, done, creationDate, version);

        //test
        ICompletedOrder test = completedOrderMapper.inputMapping(orderData);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getTicket());
        Assertions.assertNotNull(test.getItems());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(id, test.getTicket().getId());
        Assertions.assertEquals(orderId, test.getTicket().getOrderId());
        Assertions.assertEquals(creationDate, test.getTicket().getCreateAt());
        Assertions.assertEquals(version, test.getTicket().getVersion());
        Assertions.assertEquals(id, test.getTicket().getOrder().getId());
        Assertions.assertEquals(creationDate, test.getTicket().getOrder().getCreationDate());
        Assertions.assertEquals(version, test.getTicket().getOrder().getVersion());
        for (ISelectedItem output : test.getTicket().getOrder().getSelectedItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getOrderId());
            Assertions.assertEquals(id, output.getMenuItemId());
            Assertions.assertEquals(count, output.getCount());
            Assertions.assertEquals(creationDate, output.getCreateAt());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        ICompletedOrder completedOrders = new CompletedOrder(id, id, creationDate, version);

        //test
        CompletedOrderDtoCrudOutput test = completedOrderMapper.outputCrudMapping(completedOrders);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
    }

    @Test
    void outputMappingFirstCondition() {
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
        List<IPizza> pizzas = Collections.singletonList(new Pizza(id, id, name, size, creationDate, version));
        final ICompletedOrder completedOrder = new CompletedOrder(new Ticket(new Order(selectedItems, id, creationDate, version), id,
                orderId, creationDate, version), pizzas, id, id, creationDate, version);
        List<SelectedItemDtoOutput> outputs = singletonList(new SelectedItemDtoOutput(new MenuItemDtoOutput(id,
                price, id, creationDate, version, id, new PizzaInfoDtoOutput(id, name, description, size, creationDate, version)),
                id, id, id, count, creationDate, version));
        final TicketDtoOutput dtoOutput = new TicketDtoOutput(new OrderDtoOutput(outputs, id, creationDate, version),
                id, orderId, creationDate, version);
        final PizzaDtoOutput pizzaDtoOutputs = new PizzaDtoOutput(id, id, name, size, creationDate, version);
        Mockito.when(pizzaMapper.outputMapping(any(IPizza.class))).thenReturn(pizzaDtoOutputs);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(dtoOutput);

        //test
        CompletedOrderDtoOutput test = completedOrderMapper.outputMapping(completedOrder);

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
        for (PizzaDtoOutput output : test.getItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getCompletedOrderId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void outputMappingSecondCondition() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final String name = "ITALIANO PIZZA";
        final int size = 32;
        final Instant creationDate = Instant.now();
        List<IPizza> pizzas = Collections.singletonList(new Pizza(id, id, name, size, creationDate, version));
        final ICompletedOrder completedOrder = new CompletedOrder(null, pizzas, id, id, creationDate, version);
        final PizzaDtoOutput pizzaDtoOutputs = new PizzaDtoOutput(id, id, name, size, creationDate, version);
        Mockito.when(pizzaMapper.outputMapping(any(IPizza.class))).thenReturn(pizzaDtoOutputs);

        //test
        CompletedOrderDtoOutput test = completedOrderMapper.outputMapping(completedOrder);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
        for (PizzaDtoOutput output : test.getItems()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(id, output.getCompletedOrderId());
            Assertions.assertEquals(name, output.getName());
            Assertions.assertEquals(size, output.getSize());
            Assertions.assertEquals(creationDate, output.getCreationDate());
            Assertions.assertEquals(version, output.getVersion());
        }
    }

    @Test
    void outputMappingThirdCondition() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final Instant creationDate = Instant.now();
        final ICompletedOrder completedOrder = new CompletedOrder(null, null, id, id, creationDate, version);

        //test
        CompletedOrderDtoOutput test = completedOrderMapper.outputMapping(completedOrder);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(creationDate, test.getCreationDate());
        Assertions.assertEquals(version, test.getVersion());
    }
}