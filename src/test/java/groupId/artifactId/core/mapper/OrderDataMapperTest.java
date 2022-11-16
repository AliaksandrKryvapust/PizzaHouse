package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.OrderDataDtoInput;
import groupId.artifactId.core.dto.output.*;
import groupId.artifactId.core.dto.output.crud.OrderDataDtoCrudOutput;
import groupId.artifactId.dao.entity.*;
import groupId.artifactId.dao.entity.api.IOrderData;
import groupId.artifactId.dao.entity.api.IOrderStage;
import groupId.artifactId.dao.entity.api.ISelectedItem;
import groupId.artifactId.dao.entity.api.ITicket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderDataMapperTest {
    @InjectMocks
    private OrderDataMapper orderDataMapper;
    @Mock
    private OrderStageMapper orderStageMapper;
    @Mock
    private TicketMapper ticketMapper;

    @Test
    void inputMapping() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final String description = "Order accepted";
        final OrderDataDtoInput orderDataDtoInput = new OrderDataDtoInput(id, done, description);
        final IOrderStage stages = new OrderStage(description);
        Mockito.when(orderStageMapper.inputMapping(any(String.class))).thenReturn(stages);

        //test
        IOrderData test = orderDataMapper.inputMapping(orderDataDtoInput);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertNotNull(test.getOrderHistory());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.isDone());
        for (IOrderStage stage : test.getOrderHistory()) {
            Assertions.assertEquals(description, stage.getDescription());
        }
    }

    @Test
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final int version = 1;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final IOrderData orderData = new OrderData(id, id, done, creationDate, version);

        //test
        OrderDataDtoCrudOutput test = orderDataMapper.outputCrudMapping(orderData);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
        Assertions.assertEquals(creationDate, test.getCreatedAt());
        Assertions.assertEquals(version, test.getVersion());
    }

    @Test
    void outputMapping() {
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
        final MenuItemDtoOutput menuItemDtoOutput = new MenuItemDtoOutput(id,
                price, id, creationDate, version, id, new PizzaInfoDtoOutput(id, name, description, size, creationDate, version));
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).menuItemId(id).orderId(id).count(count).createdAt(creationDate).version(version).build());
        List<IOrderStage> orderStages = singletonList(new OrderStage(id, id, stageDescription, creationDate, version));
        OrderStageDtoOutput stageDtoOutputs = new OrderStageDtoOutput(id, id, stageDescription, creationDate, version);
        final IOrderData orderData = new OrderData(new Ticket(new Order(selectedItems, id, creationDate, version), id,
                orderId, creationDate, version), orderStages, id, id, done, creationDate, version);
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id, creationDate, version);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id).orderId(orderId)
                .createdAt(creationDate).version(version).build();
        Mockito.when(orderStageMapper.outputMapping(any(IOrderStage.class))).thenReturn(stageDtoOutputs);
        Mockito.when(ticketMapper.outputMapping(any(ITicket.class))).thenReturn(ticketDtoOutput);

        //test
        OrderDataDtoOutput test = orderDataMapper.outputMapping(orderData);

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

}