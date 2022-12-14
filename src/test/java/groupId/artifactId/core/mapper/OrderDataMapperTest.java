package groupId.artifactId.core.mapper;

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
    void outputCrudMapping() {
        // preconditions
        final long id = 1L;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final ITicket ticket = Ticket.builder().id(id).createAt(creationDate).build();
        final IOrderData orderData = OrderData.builder().ticket(ticket).id(id).done(done)
                .creationDate(creationDate).build();

        //test
        OrderDataDtoCrudOutput test = orderDataMapper.outputCrudMapping(orderData);

        // assert
        Assertions.assertNotNull(test);
        Assertions.assertEquals(id, test.getId());
        Assertions.assertEquals(id, test.getTicketId());
        Assertions.assertEquals(done, test.getDone());
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
        final String stageDescription = "Stage #";
        final int size = 32;
        final boolean done = false;
        final Instant creationDate = Instant.now();
        final PizzaInfo pizzaInfo = PizzaInfo.builder().name(name).description(description).size(size).build();
        final MenuItem menuItem = MenuItem.builder().id(id).pizzaInfo(pizzaInfo).price(price)
                .creationDate(creationDate).version(version).build();
        List<ISelectedItem> selectedItems = singletonList(SelectedItem.builder().id(id).menuItem(menuItem).count(count)
                .createAt(creationDate).build());
        final PizzaInfoDtoOutput pizzaInfoDtoOutput = PizzaInfoDtoOutput.builder().name(name).description(description)
                .size(size).build();
        final MenuItemDtoOutput menuItemDtoOutput = MenuItemDtoOutput.builder().id(id).price(price)
                .createdAt(creationDate).version(version).pizzaInfo(pizzaInfoDtoOutput).build();
        List<SelectedItemDtoOutput> outputs = singletonList(SelectedItemDtoOutput.builder().menuItem(menuItemDtoOutput)
                .id(id).count(count).createdAt(creationDate).build());
        List<IOrderStage> orderStages = singletonList(OrderStage.builder().id(id).description(stageDescription)
                .creationDate(creationDate).build());
        final OrderStageDtoOutput stageDtoOutputs = OrderStageDtoOutput.builder().id(id)
                .description(stageDescription).createdAt(creationDate).build();
        final Order order = new Order(id, selectedItems);
        final ITicket ticket = new Ticket(id, order, creationDate);
        final IOrderData orderData = OrderData.builder().ticket(ticket).orderHistory(orderStages).id(id).done(done)
                .creationDate(creationDate).build();
        final OrderDtoOutput orderDtoOutput = new OrderDtoOutput(outputs, id);
        final TicketDtoOutput ticketDtoOutput = TicketDtoOutput.builder().order(orderDtoOutput).id(id)
                .createdAt(creationDate).build();
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
            Assertions.assertEquals(name, output.getMenuItem().getPizzaInfo().getName());
            Assertions.assertEquals(description, output.getMenuItem().getPizzaInfo().getDescription());
            Assertions.assertEquals(size, output.getMenuItem().getPizzaInfo().getSize());
        }
        for (OrderStageDtoOutput output : test.getOrderHistory()) {
            Assertions.assertEquals(id, output.getId());
            Assertions.assertEquals(stageDescription, output.getDescription());
            Assertions.assertEquals(creationDate, output.getCreatedAt());
        }
    }

}